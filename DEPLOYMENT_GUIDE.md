# Deployment Guide

This guide provides step-by-step instructions for deploying the Business Management API to various cloud platforms.

## Prerequisites

- Git repository with your code
- Docker Hub account (optional, for custom images)
- Cloud platform account (Render, Railway, AWS, etc.)

## Environment Variables

All deployments require these environment variables:

```env
# Database Configuration
DB_URL=jdbc:postgresql://host:port/database
DB_USER=your_db_user
DB_PASSWORD=your_db_password

# JWT Configuration
JWT_SECRET=your-256-bit-secret-key-here

# Application Profile
SPRING_PROFILE=prod

# Optional: Server Port (default: 8080)
SERVER_PORT=8080
```

## 1. Deploy to Render

### Step 1: Create PostgreSQL Database

1. Log in to [Render Dashboard](https://dashboard.render.com/)
2. Click "New +" → "PostgreSQL"
3. Configure:
   - Name: `businessapp-db`
   - Database: `businessdb`
   - User: `businessapp`
   - Region: Choose closest to your users
   - Plan: Free or paid
4. Click "Create Database"
5. Copy the "Internal Database URL" for later use

### Step 2: Create Web Service

1. Click "New +" → "Web Service"
2. Connect your GitHub repository
3. Configure:
   - Name: `business-management-api`
   - Region: Same as database
   - Branch: `main`
   - Runtime: `Docker`
   - Plan: Free or paid

### Step 3: Configure Environment Variables

Add these environment variables:

```
DB_URL=<Internal Database URL from Step 1>
DB_USER=businessapp
DB_PASSWORD=<from database creation>
JWT_SECRET=<generate a secure random string>
SPRING_PROFILE=prod
```

### Step 4: Deploy

1. Click "Create Web Service"
2. Render will automatically build and deploy
3. Monitor logs for any issues
4. Access your API at: `https://your-service-name.onrender.com`

### Step 5: Verify Deployment

```bash
curl https://your-service-name.onrender.com/actuator/health
```

## 2. Deploy to Railway

### Step 1: Install Railway CLI (Optional)

```bash
npm install -g @railway/cli
railway login
```

### Step 2: Create New Project

**Via Web Dashboard:**
1. Go to [Railway Dashboard](https://railway.app/)
2. Click "New Project"
3. Select "Deploy from GitHub repo"
4. Choose your repository

**Via CLI:**
```bash
railway init
railway link
```

### Step 3: Add PostgreSQL Database

1. In your project, click "New"
2. Select "Database" → "PostgreSQL"
3. Railway will automatically create and configure the database

### Step 4: Configure Environment Variables

**Via Web Dashboard:**
1. Click on your service
2. Go to "Variables" tab
3. Add:
   ```
   JWT_SECRET=your-secret-key
   SPRING_PROFILE=prod
   ```

**Via CLI:**
```bash
railway variables set JWT_SECRET=your-secret-key
railway variables set SPRING_PROFILE=prod
```

Note: Railway automatically sets `DATABASE_URL`. You may need to transform it:

```bash
# Railway provides DATABASE_URL in format:
# postgresql://user:password@host:port/database

# Transform to JDBC format in your application or use:
DB_URL=${{DATABASE_URL}}
```

### Step 5: Deploy

**Via Web Dashboard:**
- Push to your GitHub repository
- Railway auto-deploys on push

**Via CLI:**
```bash
railway up
```

### Step 6: Get Deployment URL

```bash
railway domain
```

Or check the dashboard for your public URL.

## 3. Deploy to AWS ECS (Fargate)

### Prerequisites

- AWS CLI installed and configured
- Docker installed locally
- AWS account with appropriate permissions

### Step 1: Create ECR Repository

```bash
# Create ECR repository
aws ecr create-repository --repository-name business-management-api

# Get repository URI
ECR_URI=$(aws ecr describe-repositories --repository-names business-management-api --query 'repositories[0].repositoryUri' --output text)
```

### Step 2: Build and Push Docker Image

```bash
# Login to ECR
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin $ECR_URI

# Build image
docker build -t business-management-api .

# Tag image
docker tag business-management-api:latest $ECR_URI:latest

# Push image
docker push $ECR_URI:latest
```

### Step 3: Create RDS PostgreSQL Database

```bash
# Create DB subnet group
aws rds create-db-subnet-group \
  --db-subnet-group-name businessapp-subnet-group \
  --db-subnet-group-description "Subnet group for business app" \
  --subnet-ids subnet-xxxxx subnet-yyyyy

# Create RDS instance
aws rds create-db-instance \
  --db-instance-identifier businessapp-db \
  --db-instance-class db.t3.micro \
  --engine postgres \
  --engine-version 15.3 \
  --master-username postgres \
  --master-user-password YourSecurePassword123 \
  --allocated-storage 20 \
  --db-subnet-group-name businessapp-subnet-group \
  --vpc-security-group-ids sg-xxxxx
```

### Step 4: Create ECS Task Definition

Create `task-definition.json`:

```json
{
  "family": "business-management-api",
  "networkMode": "awsvpc",
  "requiresCompatibilities": ["FARGATE"],
  "cpu": "256",
  "memory": "512",
  "containerDefinitions": [
    {
      "name": "business-management-api",
      "image": "<ECR_URI>:latest",
      "portMappings": [
        {
          "containerPort": 8080,
          "protocol": "tcp"
        }
      ],
      "environment": [
        {
          "name": "SPRING_PROFILE",
          "value": "prod"
        },
        {
          "name": "DB_URL",
          "value": "jdbc:postgresql://<RDS_ENDPOINT>:5432/businessdb"
        },
        {
          "name": "DB_USER",
          "value": "postgres"
        },
        {
          "name": "DB_PASSWORD",
          "value": "YourSecurePassword123"
        },
        {
          "name": "JWT_SECRET",
          "value": "your-256-bit-secret-key"
        }
      ],
      "logConfiguration": {
        "logDriver": "awslogs",
        "options": {
          "awslogs-group": "/ecs/business-management-api",
          "awslogs-region": "us-east-1",
          "awslogs-stream-prefix": "ecs"
        }
      }
    }
  ]
}
```

Register task definition:

```bash
aws ecs register-task-definition --cli-input-json file://task-definition.json
```

### Step 5: Create ECS Cluster

```bash
aws ecs create-cluster --cluster-name business-management-cluster
```

### Step 6: Create Application Load Balancer

```bash
# Create ALB
aws elbv2 create-load-balancer \
  --name business-management-alb \
  --subnets subnet-xxxxx subnet-yyyyy \
  --security-groups sg-xxxxx

# Create target group
aws elbv2 create-target-group \
  --name business-management-tg \
  --protocol HTTP \
  --port 8080 \
  --vpc-id vpc-xxxxx \
  --target-type ip \
  --health-check-path /actuator/health

# Create listener
aws elbv2 create-listener \
  --load-balancer-arn <ALB_ARN> \
  --protocol HTTP \
  --port 80 \
  --default-actions Type=forward,TargetGroupArn=<TARGET_GROUP_ARN>
```

### Step 7: Create ECS Service

```bash
aws ecs create-service \
  --cluster business-management-cluster \
  --service-name business-management-service \
  --task-definition business-management-api \
  --desired-count 2 \
  --launch-type FARGATE \
  --network-configuration "awsvpcConfiguration={subnets=[subnet-xxxxx,subnet-yyyyy],securityGroups=[sg-xxxxx],assignPublicIp=ENABLED}" \
  --load-balancers targetGroupArn=<TARGET_GROUP_ARN>,containerName=business-management-api,containerPort=8080
```

### Step 8: Access Your Application

Get ALB DNS name:

```bash
aws elbv2 describe-load-balancers --names business-management-alb --query 'LoadBalancers[0].DNSName' --output text
```

## 4. Deploy to Heroku

### Step 1: Install Heroku CLI

```bash
# macOS
brew tap heroku/brew && brew install heroku

# Or download from https://devcenter.heroku.com/articles/heroku-cli
```

### Step 2: Login and Create App

```bash
heroku login
heroku create business-management-api
```

### Step 3: Add PostgreSQL

```bash
heroku addons:create heroku-postgresql:mini
```

### Step 4: Set Environment Variables

```bash
heroku config:set JWT_SECRET=your-secret-key
heroku config:set SPRING_PROFILE=prod
```

### Step 5: Deploy

```bash
# Using Heroku Git
git push heroku main

# Or using Docker
heroku container:push web
heroku container:release web
```

### Step 6: Open Application

```bash
heroku open
```

## 5. Deploy to Google Cloud Run

### Step 1: Build and Push to GCR

```bash
# Set project
gcloud config set project YOUR_PROJECT_ID

# Build image
gcloud builds submit --tag gcr.io/YOUR_PROJECT_ID/business-management-api

# Or use Docker
docker build -t gcr.io/YOUR_PROJECT_ID/business-management-api .
docker push gcr.io/YOUR_PROJECT_ID/business-management-api
```

### Step 2: Create Cloud SQL Instance

```bash
gcloud sql instances create businessapp-db \
  --database-version=POSTGRES_15 \
  --tier=db-f1-micro \
  --region=us-central1
```

### Step 3: Deploy to Cloud Run

```bash
gcloud run deploy business-management-api \
  --image gcr.io/YOUR_PROJECT_ID/business-management-api \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --set-env-vars "SPRING_PROFILE=prod,JWT_SECRET=your-secret-key,DB_URL=jdbc:postgresql:///<DATABASE>?cloudSqlInstance=<INSTANCE_CONNECTION_NAME>&socketFactory=com.google.cloud.sql.postgres.SocketFactory"
```

## Post-Deployment Checklist

- [ ] Verify health endpoint: `curl https://your-domain/actuator/health`
- [ ] Test user registration
- [ ] Test user login
- [ ] Test creating an invoice
- [ ] Check database migrations ran successfully
- [ ] Verify CORS settings for your frontend
- [ ] Set up monitoring and alerts
- [ ] Configure custom domain (if needed)
- [ ] Set up SSL/TLS certificate
- [ ] Configure backup strategy for database
- [ ] Set up logging aggregation
- [ ] Document your deployment process

## Troubleshooting

### Application Won't Start

1. Check logs for errors
2. Verify all environment variables are set
3. Ensure database is accessible
4. Check JWT_SECRET is at least 256 bits

### Database Connection Issues

1. Verify DB_URL format is correct
2. Check database credentials
3. Ensure database allows connections from your app
4. Verify security groups/firewall rules

### 502/503 Errors

1. Check if application is listening on correct port
2. Verify health check endpoint is accessible
3. Check application logs for startup errors
4. Ensure sufficient memory/CPU allocated

## Security Recommendations

1. **Use Secrets Management**: Store sensitive data in platform secret managers
2. **Enable HTTPS**: Always use SSL/TLS in production
3. **Rotate JWT Secret**: Regularly rotate your JWT secret
4. **Database Backups**: Enable automated backups
5. **Monitoring**: Set up application and infrastructure monitoring
6. **Rate Limiting**: Implement rate limiting to prevent abuse
7. **WAF**: Consider using a Web Application Firewall
8. **Regular Updates**: Keep dependencies up to date

## Scaling Considerations

1. **Horizontal Scaling**: Add more instances as traffic grows
2. **Database Connection Pooling**: Configure appropriate pool size
3. **Caching**: Add Redis for frequently accessed data
4. **CDN**: Use CDN for static assets
5. **Load Balancing**: Distribute traffic across instances
6. **Database Read Replicas**: For read-heavy workloads

## Support

For deployment issues:
1. Check platform-specific documentation
2. Review application logs
3. Verify environment configuration
4. Open an issue in the repository

