# ---------------------------------------------------------------------------------------------------------------------
# AWS PROVIDER FOR TF CLOUD
# ---------------------------------------------------------------------------------------------------------------------

terraform {
    required_version = "~>1.2"
}

provider "aws" {
    region  = var.aws_region
    profile = var.aws_profile
}
