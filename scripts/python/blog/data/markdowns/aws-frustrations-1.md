## Beginner Issues when getting Started with AWS - Part 1

### Introduction
AWS is used throughout the tech industry, either supplying a subset or the entirety of a company's infrastructure. With 
that in mind, most Software Engineers will not be able to get out of learning how to use the various services 
provided by AWS. In this post, I'll go over my experience with AWS when trying to get a website and microservice up and 
running, as well as, some links to guides that I used to help me.

To set some context, I've worked in the tech industry for the last 7 years and have worked at 2 separate companies 
that have used AWS. In my experiences, these companies already had established processes and running services, so my 
opportunity to create a website start-to-finish has been non-existent. I had some free time recently, so I figured I 
might as well try now. Let me tell you, I have never felt such disdain towards AWS as I have felt in the last week. 
Since this was a side project, I wanted to limit the costs as much as possible. So my focus was using as many of the 
free tier services are I could. With that in mind, some of my frustrations could have been alleviated by using some of 
the more expensive features in AWS.

### The Calm Before the Storm
When I decided to create this website, I already had an idea of the frameworks that I would be working with. 

* NextJS + React + Typescript for the frontend. 

* Gradle + Spring + Java + Docker for the backend. 

* Python / Bash for various build / deploy scripts. 

So to start, I quickly got a basic frontend up and running. NextJS is super simple to get started and getting a working 
website up and running take almost no time at all. More information can be found 
[here](https://nextjs.org/docs#automatic-setup).

After having a basic frontend, I decided to get setup with a basic api microservice. Getting setup with spring took a 
bit more time than I initially thought it would take, but after a bit of time I had my first API up and running 
locally. I think part of my issues here were associated with not wanting to use large starter packages that are common 
in guides. But eventually, I caved and used the spring-boot-starter as the purpose of this side project was to better 
learn AWS, not fight Spring. More information can be found [here](https://spring.io/guides/gs/spring-boot/).

Now that everything was up and running locally, I spent a few minutes getting the scripts / configs setup to have the 
Spring service dockerized. More information can be found [here](https://github.com/docker/getting-started). 
At the time, I thought this was forward-thinking, but this was actually the beginning of the AWS frustration.

### The Beginning
I wanted to start by getting my service up and running on a remote server. Looking through AWS, I saw that there was 
free tier in [ECR](https://aws.amazon.com/ecr/), which would be perfect for my dockerized service. Getting ECR setup 
via the AWS-CLI was incredibly simple. Within a few minutes, I had my image present on ECR with setting to automatically 
overwrite past images (to keep the image space down). The AWS docs were surprisingly easy to follow and simple to 
understand, more info [here](https://docs.aws.amazon.com/AmazonECR/latest/userguide/getting-started-cli.html).

Now that my image was present in AWS, I figured that getting that image running on a server would be a piece of cake. I 
was terribly wrong. Looking between EC2 (which has a free tier) and ECS (which doesn't), I decided to start with ECS 
since it was advertised as an image runner that would limit costs and determine if the price would be too much. 
A few cents a month wouldn't bother me for a service that was really only running for my own purposes. So I got a basic 
cluster setup with a long-running task definition with the minimum hardware. There were some issues with the vpc and 
security groups, but I'll touch on that in part 2. 

At this point, I could easily set up my environment variables to load from secret manager into my container 
([info here](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/specifying-sensitive-data-secrets.html)). 
Starting the task was easy and seemed to work. With REALLY loose security settings, I now had access to my service on 
AWS. AWESOME! 

### Trying to Automate
Not wanting to have to manually build my image, push to the repository, and then manually restart my task, I decided to 
try out CodePipeline. This AWS service could easily hook into my GitHub, monitor a branch, build that branch via a 
config file, and then directly deploy to ECS. 
Github info [here](https://docs.aws.amazon.com/codepipeline/latest/userguide/connections-github.html). 
Deploy to ECS info [here](https://docs.aws.amazon.com/codepipeline/latest/userguide/ecs-cd-pipeline.html). 
Using this start to finish appeared to work pretty well, so I did some rapid updates to my service and called it a day. 

The next day I checked my billing to find over a $1 in charges for ECS. This was way too high. I noticed that the 
build step of the pipeline was using a lot of ECS resources and at this point I couldn't believe my idle service was 
that expensive. So I decided to build and push the image locally. From there the pipeline would automatically deploy 
that image. This seemed like a good solution for a budget, with some good automation to help limit my interactions with 
AWS. So I rebuilt my pipeline, pushed my image manually, and the pipeline failed. The error stated that "The image 
definition file images.json contents invalid JSON format", which confused me as this file is generated by AWS in ECR 
and has nothing to do with me. It turns out that you can't manually build an image and then use code pipeline to deploy 
the image because the images.json file will just be the wrong format. WHAT?!?! The only way to fix this is to add a 
build step and modify the contents of the images.json file, which would still use ECS resources. How is it possible that 
two AWS services could be so incompatible.

At this point I was stumped and decided to not use automation since clicking a button wasn't the end of the world. If I 
happen to make some ad revenue in the future I can think about having a budget to automate. At this point, I had wasted 
a few hours bashing my head into the wall and decided to call it a day.

Continue on to part 2, where I will discuss ECS, IAM, and EC2.

