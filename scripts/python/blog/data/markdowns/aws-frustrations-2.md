## Beginner Issues when getting Started with AWS - Part 2

### Introduction
This is a continuation of a multipart article. To go to part 1 either click 
[here](https://programmingbean.com/blog/aws-frustrations-1) or scroll down to the bottom and click previous.

### ECS and the Misleading Marketing
In general, ECS is marketed as the easier and cheaper alternative to EC2. ECS certainly has more features, more 
interconnectivity within AWS, and is probably cheaper in some situations (most likely large businesses). In the case 
of a small blog, this is not the case. Much of the marketing claims that since ECS is dynamic with its recourses your 
services use less resources and are therefore, cheaper to run. However, with the lack of a free tier and a slightly 
increased price tag, this doesn't always work out if you are utilizing your resources intelligently.

In my case, removing the build step of my automation reduced with ECS bill by half; however this meant that I was 
paying $0.50 a day to have a borderline idle service running. This just wouldn't do. 

### Moving to EC2
At this point, ECS wasn't economical for a small project. So I had to move to a free tier AWS service that could 
support my backend / frontend. EC2 was the obvious answer, but it led to the immediate problem that I would lose a lot 
of automation that I had previously had with ECS. Luckily I had already invested in startup scripts, so moving to a 
more manual implementation wasn't going to be a huge issues.

The first step was getting the [free tier](https://aws.amazon.com/free/) EC2 setup and 
[accessible](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/AccessingInstancesLinux.html). 
This was surprisingly easy. After getting everything setup locally, I just had to set up a quick alias to connect to 
my EC2 instance quickly. 

Now that I could access my EC2 instance, I had to get it setup to run my images. AWS EC2 comes with multiple options of 
[OSs](https://docs.aws.amazon.com/systems-manager/latest/userguide/prereqs-operating-systems.html) that one could use. 
In my case, I decided to use Amazon's own Linux distro, since it has many useful packages already installed and has 
AWS CLI already setup.  On top of what was already present, I had to install:

* Docker to run my backend images.

* PM2 as a daemon for my frontend and cron scripts.

* Node to build / run my frontend image.

* Git to pull the source code.

Once everything was installed and my source code was present on EC2, I added my necessary environment variables and 
finalized the bash scripts to build and run the backend and frontend. Success!! At this point, I finally had my code 
running on AWS in an affordable way. With the scripts and runners, I could relatively easily build and deploy my 
code.

### Connecting your EC2 Instance to your Domain
This was an entire ordeal. There is no other way to put it. 

The first step is to get a domain from any domain provider. In my case, I decided to use 
[route 53](https://aws.amazon.com/route53/)  on AWS since I had already decided to go all in on AWS for this project. 
It was very easy to find an available domain and purchase it. 

Before going any further, at this point you need to decide if you want to talk to your EC2 instance directly or through 
a load balancer. Realistically, all businesses will use architecture that includes a load balancer. Since I wanted to 
continue to learn, I decided to add a load balancer in front of my single instance. This 
[load balancer](https://aws.amazon.com/elasticloadbalancing/) is also managed via EC2, and is the service that will 
be exposed externally to communicate with the outside internet.

At this point there was a lot of IAM, Route 53, and Certification issues that were hammered away at for hours on end to 
eventually get my domain to communicate to my backend / frontend via HTTP and then eventually to HTTPS. Rather than 
trying to transcribe what steps I did, I will instead post a videos that include steps that I followed to get 
everything working.

* [Overview Video](https://www.youtube.com/watch?v=JQP96EjRM98) - A comprehensive guide through Route 53, HTTPS,
and ELB. Highly recommend.

* [Specifics on Route 53 Video](https://www.youtube.com/watch?v=ookzXuMr8eY) - A deep dive on Route 53.

* [HTTPS Specific Video](https://www.youtube.com/watch?v=QA4X8ntcNBQ) - Wasn't useful for me, but covers WordPress.

One thing to note, that I don't think is correctly mentioned in the above videos, is that getting the certification 
set up can take a long time. You need to follow all the instructions completely, or it will never resolve. It is also 
VERY important to keep an eye on your email and validate your email for your Route 53 domain, or else AWS will just 
block all traffic to your domain after 15 days. They don't notify you, and it can be a pain to track down the correct 
page for the validation email.

At this point my domain connected to both my backend and my frontend. So for the first time I could relax and 
celebrate. I added a few customizations for my service, the most important one being a set of rules in ELB to direct 
traffic to my backend if the host path started with 'backend.'. For everything else I directed it to my frontend. You 
can test this by going to:

* Frontend - [https://www.programmingbean.com](https://www.programmingbean.com)

* Backend - [https://backend.programmingbean.com/health](https://backend.programmingbean.com/health)

Continue on to part 3, where I will discuss AWS and Spring.

