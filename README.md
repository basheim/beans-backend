Setup EC2

* sudo yum update -y
* sudo amazon-linux-extras install docker
* sudo service docker start
* sudo usermod -a -G docker ec2-user
* sudo setfacl --modify user:ec2-user:rw /var/run/docker.sock
* sudo yum install git
* sudo yum install jq
* aws configure with ec2/linuxuser account

Setup git ssh
* ssh-keygen -t ed25519 -C "brandonasheim@gmail.com"
* eval "$(ssh-agent -s)"
* ssh-add ~/.ssh/id_ed25519
* cat ~/.ssh/id_ed25519.pub

Clone git repo
* git clone
* 