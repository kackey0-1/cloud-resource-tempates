# Build and Deploy Spring Petclinic Application to AWS App Runner using AWS CodePipeline, Amazon RDS and Terraform
## Architecture
![Architecture](images/Architecture.png)

## Local Execution
```bash
./gradlew check
./gradlew build -x test
java -Djava.security.egd=file:/dev/./urandom -jar build/libs/aws-app-runner-0.0.1-SNAPSHOT.jar
```

## Deploy
### Install necessary resouces

```bash
aws --version
pip install awscli --upgrade --user
# install jdk17 for amazon linux 2
wget https://download.java.net/java/GA/jdk17/0d483333a00540d886896bac774ff48b/35/GPL/openjdk-17_linux-x64_bin.tar.gz
tar xvf openjdk-17_linux-x64_bin.tar.gz
sudo mv jdk-17 /opt/
sudo tee /etc/profile.d/jdk.sh <<EOF
export JAVA_HOME=/opt/jdk-17
export PATH=$PATH:$JAVA_HOME/bin
EOF

sudo yum install -y yum-utils
sudo yum-config-manager --add-repo https://rpm.releases.hashicorp.com/AmazonLinux/hashicorp.repo
# terraform over 1.2
sudo yum -y install terraform

aws configure
```

### Build
```bash
cd ~/cloud-resource-templates/aws-app-runner
./gradlew check
./gradlew build -x test --no-build-cache
docker build -t petclinic .
```

### Terraform
```bash
# Set up SSM parameter for DB passwd
aws ssm put-parameter --name /database/password  --value mysqlpassword --type SecureString

terraform init

terraform plan
# before apply services
# build must be done
terraform apply
```

### Git Setup

```bash
cd ~/environment/aws-app-runner
git config --global user.name "Your Name"
git config --global user.email you@example.com

git init
git add .
git commit -m "Baseline commit"

### Set up the remote CodeCommit repo
# An AWS CodeCommit repo was built as part of the pipeline you created. You will now set this up as a remote repo for your local petclinic repo.
# For authentication purposes, you can use the AWS IAM git credential helper to generate git credentials based on your IAM role permissions. Run:
git config --global credential.helper '!aws codecommit credential-helper $@'
git config --global credential.UseHttpPath true

# From the output of the Terraform build, we use the output `source_repo_clone_url_http` in our next step.

cd ~/environment/aws-app-runner/terraform
export tf_source_repo_clone_url_http=$(terraform output --raw source_repo_clone_url_http)
cd ~/environment/aws-app-runner
git remote add origin $tf_source_repo_clone_url_http
git remote -v
```

---


#### Clone workshop repository
<!---
You will need to import the workshop files into your Cloud9 environment:

```bash
wget https://github.com/aws-samples/aws-ecs-cicd-terraform/archive/master.zip
unzip master.zip
cd aws-ecs-cicd-terraform-master
```
-->

Clone the source code repository:

```bash
cd ~/environment
git clone https://github.com/aws-samples/aws-app-runner.git
```

---

## Build and tag the Petclinic docker image
From the petclinic directory:

```bash
docker build -t petclinic .
```

## Run Petclinic application locally
Run the following inside the Cloud9 terminal:

```bash
docker run -it --rm -p 8080:80  --name petclinic petclinic
```
![ApplicationLocal](images/docker-local-run.png)

This will run the application using container port of 80 and will expose the application to host port of 8080. Click Preview from the top menu and then click “Preview Running Application.” It will open a browser displaying the Spring Petclinic application.

## Push Petclinic docker image to Amazon ECR
On your Cloud9 IDE open a new terminal and run the following inside the new terminal:

```bash
AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query 'Account' --output text)
AWS_REGION=$(aws configure get region)

export REPOSITORY_NAME=petclinic
export IMAGE_NAME=petclinic

aws ecr create-repository \
    --repository-name $REPOSITORY_NAME \
    --image-scanning-configuration scanOnPush=true \
    --region $AWS_REGION

aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com

docker tag $IMAGE_NAME $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$IMAGE_NAME
docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$IMAGE_NAME

```

## Build the infrastructure and pipeline

We shall use Terraform to build the above architecture including the AWS CodePipeline.

**Note:** This workshop will create chargeable resources in your account. When finished, please make sure you clean up resources as instructed at the end.

### Set up SSM parameter for DB passwd

```bash
aws ssm put-parameter --name /database/password  --value mysqlpassword --type SecureString
```

### Edit terraform variables

```bash
cd ~/environment/aws-app-runner/terraform
```

Edit `terraform.tfvars`, leave the `aws_profile` as `"default"`, and ensure `aws_region` matches your environment, and update `codebuild_cache_bucket_name` to replace the placeholder `yyyymmdd` with today's date, and the identifier `identifier` with something unique to you to create globally unique S3 bucket name. S3 bucket names can include numbers, lowercase letters and hyphens.

### Build

Initialise Terraform:

```bash
terraform init
```

Build the infrastructure and pipeline using terraform:

```bash
terraform apply
```

Terraform will display an action plan. When asked whether you want to proceed with the actions, enter `yes`.

Wait for Terraform to complete the build before proceeding. It will take few minutes to complete “terraform apply”

### Explore the stack you have built

Once the build is complete, you can explore your environment using the AWS console:
- View the App Runner service using the [AWS App Runner console](https://console.aws.amazon.com/apprunner/)
- View the RDS database using the [Amazon RDS console](https://console.aws.amazon.com/rds).
- View the ECR repo using the [Amazon ECR console](https://console.aws.amazon.com/ecr).
- View the CodeCommit repo using the [AWS CodeCommit console](https://console.aws.amazon.com/codecommit).
- View the CodeBuild project using the [AWS CodeBuild console](https://console.aws.amazon.com/codebuild).
- View the pipeline using the [AWS CodePipeline console](https://console.aws.amazon.com/codepipeline).


Note that your pipeline starts in a failed state. That is because there is no code to build in the CodeCommit repo! In the next step you will push the petclinic app into the repo to trigger the pipeline.

### Explore the App Runner service
Open the App Runner service configuration file [terraform/services.tf](terraform/services.tf) file and explore the options specified in the file.

```typescript
 image_repository {
      image_configuration {
        port = var.container_port
        runtime_environment_variables = {
          "spring.datasource.username" : "${var.db_user}",
          "spring.datasource.password" : "${data.aws_ssm_parameter.dbpassword.value}",
          "spring.datasource.initialization-mode" : var.db_initialize_mode,
          "spring.profiles.active" : var.db_profile,
          "spring.datasource.url" : "jdbc:mysql://${aws_db_instance.db.address}/${var.db_name}"
        }
      }
      image_identifier      = "${data.aws_ecr_repository.image_repo.repository_url}:latest"
      image_repository_type = "ECR"
    }
```
**Note:** In a production environment it is a best practice to use a meaningful tag instead of using the `:latest` tag.

## Deploy petclinic application using the pipeline

You will now use git to push the petclinic application through the pipeline.



### Set up a local git repo for the petclinic application

Start by switching to the `petclinic` directory:

```bash
cd ~/environment/aws-app-runner/petclinic
```

Set up your git username and email address:

```bash
git config --global user.name "Your Name"
git config --global user.email you@example.com
```

Now ceate a local git repo for petclinic as follows:

```bash
git init
git add .
git commit -m "Baseline commit"
```

### Set up the remote CodeCommit repo

An AWS CodeCommit repo was built as part of the pipeline you created. You will now set this up as a remote repo for your local petclinic repo.

For authentication purposes, you can use the AWS IAM git credential helper to generate git credentials based on your IAM role permissions. Run:

```bash
git config --global credential.helper '!aws codecommit credential-helper $@'
git config --global credential.UseHttpPath true
```

From the output of the Terraform build, we use the output `source_repo_clone_url_http` in our next step.

```bash
cd ~/environment/aws-app-runner/terraform
export tf_source_repo_clone_url_http=$(terraform output --raw source_repo_clone_url_http)
```

Set this up as a remote for your git repo as follows:

```bash
cd ~/environment/aws-app-runner/petclinic
git remote add origin $tf_source_repo_clone_url_http
git remote -v
```

You should see something like:

```bash
origin  https://git-codecommit.eu-west-2.amazonaws.com/v1/repos/petclinic (fetch)
origin  https://git-codecommit.eu-west-2.amazonaws.com/v1/repos/petclinic (push)
```


### Trigger the pipeline

To trigger the pipeline, push the master branch to the remote as follows:

```bash
git push -u origin master
```

The pipeline will pull the code, build the docker image, push it to ECR, and deploy it to your ECS cluster. This will take a few minutes.
You can monitor the pipeline in the [AWS CodePipeline console](https://console.aws.amazon.com/codepipeline).


### Test the application

From the output of the Terraform build, note the Terraform output `apprunner_service_url`.

```bash
cd ./terraform
export tf_apprunner_service_url=$(terraform output apprunner_service_url)
echo $tf_apprunner_service_url
```

Use this in your browser to access the application.

![Petclinic](images/petclinic.png)
## Push a change through the pipeline and re-test

The pipeline can now be used to deploy any changes to the application.

You can try this out by changing the welcome message as follows:

```
cd ~/environment/aws-app-runner/petclinic
vi src/main/resources/messages/messages.properties
```
Change the value for the welcome string, for example, to "Hello".

Commit the change:

```
git add .
git commit -m "Changed welcome string"
```

Push the change to trigger pipeline:

```bash
git push origin master
```

As before, you can use the console to observe the progression of the change through the pipeline. Once done, verify that the application is working with the modified welcome message.

## Tearing down the stack

**Note:** If you are participating in this workshop at an AWS-hosted event using Event Engine and a provided AWS account, you do not need to complete this step. We will cleanup all managed accounts afterwards on your behalf.

Make sure that you remember to tear down the stack when finshed to avoid unnecessary charges. You can free up resources as follows:

```
cd ~/environment/aws-app-runner/terraform
terraform destroy
```

When prompted enter `yes` to allow the stack termination to proceed.

Once complete, note that you will have to manually empty and delete the S3 bucket used by the pipeline.

## Delete the Amazon ECR

```bash
aws ecr delete-repository \
    --repository-name $REPOSITORY_NAME \
    --region $AWS_REGION \
    --force
```
