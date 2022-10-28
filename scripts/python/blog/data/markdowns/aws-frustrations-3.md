## Beginner Issues when getting Started with AWS - Part 3

### Introduction
This is a continuation of a multipart article. To go to part 2 either click 
[here](https://programmingbean.com/blog/aws-frustrations-2) or scroll down to the bottom and click previous.

### Summary
In the previous posts, I mentioned the frustrations associated with AWS and AWS's services. This post is going to focus 
more on the learnings from using Spring, Java, and AWS. In general, it was a mix bag, with some parts being easy to use 
and understand, while others were not well documented or confusing.

### AWS Developer Docs
To begin, we should talk about AWS developer docs and guide that are supplied by AWS itself. The best way to describe 
these guides is thorough. I truly believe that you can find every piece of information that you need to interact with 
AWS from their guides, but sometimes too much information is a bad thing. I found that though the docs were complete, 
I had trouble finding the information I needed without spending a long time reading or navigating confusing 
documentation.

Examples:

* [AWS Blog on Spring RDS](https://aws.amazon.com/blogs/opensource/using-a-postgresql-database-with-amazon-rds-and-spring-boot/)

* [Java Developer Guide](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/home.html)

One piece of missing information (outside of the AWS blog), was the best way to interact with AWS via Spring, which 
is expected (as you can't expect there to be a guide for everything), but with Spring being one of (if not the) most 
used Java web framework, you may expect some documentation. For the most part, I had to use third party guides to 
help me with understanding how to use AWS on Spring.

### Spring's Magic
After becoming familiar with Node, Python, and other Java libraries, I can say for certain that Spring has some 
amazingly indepth and well encapsulated features. That being said, it suffers heavily (probably the most I have ever 
seen) from what I will refer to as "Magic". Spring obfuscates so much of its logic away for the user, that 
sometimes it isn't clear how things work or what is all being done by the package. Since there isn't a clear way to 
find the answer without reading through the Spring code, when asked how Spring works, I often respond with "Magic". 

This magic makes it real difficult to get a full understanding of how super complex concepts work at the base level. 
In the case of Spring AWS libraries, all of them are third party libraries. These libraries make it simple to connect 
Spring to AWS and make up the majority of the guides for how to get AWS and Springs working together, 
but have a few major drawbacks. Neither of the libraries cover all AWS use cases. Often the libraries require many 
specific configs to be setup, which are sometimes not specifically mentioned in the documentation. The errors when 
configs are missing are misleading and often just say that AWS could not be connected to. Though I am fairly sure these 
libraries are safe, it is always a concern when using third party libraries to connect to data. If the data I was 
interacting with was confidential, I would never use these libraries and would just suffer through the AWS 
documentation.

Two Main Libraries:

* [Legacy spring-cloud-aws](https://github.com/awspring/spring-cloud-aws)

* [awspring.io](https://awspring.io/)

### Comparison
Overall, your only two options are AWS's SDK, which is overly complex and requires a indepth understanding of Spring, or 
third party libraries, which obfuscate too much of the logic and don't cover all use cases. There really isn't a middle 
ground in this particular area. For my use cases, I went with the libraries, as I wasn't storing any confidential 
information and my use cases were covered by the third party libraries.

### Positives

I spent a lot of these posts complaining about Spring and AWS, so I want to conclude with the positives. AWS has an 
amazing range of features and indepth documentation to help anyone navigate these features. If you are willing to pay 
the price, your service can easily be integrated throughout AWS. My experience managing my services through AWS has 
been simple and stable. AWS makes billing easy to review and complete, and I have never had an issue with free tier 
services charging me.

Spring makes so many complex topics and features super easy. Once you get used to Spring, it is easy to modify the 
configs and make the service exactly how you want to it run. There is a large and active community that makes many 
libraries and guides.

### Conclusion

Would I recommend using AWS for a super low cost set of services? No, probably not. I've used other free tier services 
from Google and from Vercel (creator of NextJS). Google's free tier is way easier to navigate and I found the Google 
SDK to be much easier to use and understand. Vercel is mostly for web hosting, but it was so easy to get setup with 
NextJS I can't help but call it out. Overall, AWS seems powerful, but was definitely way more complicated that similar 
services when you have a low budget.

