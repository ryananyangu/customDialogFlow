FROM ubuntu

# Use bash, better than sh
SHELL ["/bin/bash", "-c"]

RUN apt-get update && apt-get dist-upgrade -y

# Install required packages
RUN apt-get install -y gnupg curl

# Add the Cloud SDK distribution URI as a package source
RUN echo "deb [signed-by=/usr/share/keyrings/cloud.google.gpg] http://packages.cloud.google.com/apt cloud-sdk main" | tee -a /etc/apt/sources.list.d/google-cloud-sdk.list

# Import the Google Cloud Platform public key
RUN curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | apt-key --keyring /usr/share/keyrings/cloud.google.gpg add -


# Install google cloud SDK
RUN apt-get update
RUN apt-get install -y google-cloud-sdk google-cloud-sdk-app-engine-java

# Install maven
RUN apt-get install -y maven

