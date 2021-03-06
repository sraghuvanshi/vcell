## INFO [build containers info (do this later)](../swarm/README.md): builds the Docker images (1-9 below) and pushes them into a Docker registry (e.g. namespace = "vcell-docker.cam.uchc.edu:5000") with the image tag derived from the Git commit hash at build time (e.g. tag = "392af4d").  The vcell-batch Singularity image (item 10 below) is built from the vcell-batch Docker image for use within a HPC environment.

1) vcell-api         => docker image in registry    (api)  
2) vcell-db          => docker image in registry    (db)  
3) vcell-sched       => docker image in registry    (sched)  
4) vcell-submit      => docker image in registry    (submit)  
5) vcell-mongodb     => docker image in regsitry    (mongodb)  
6) vcell-activemqint => docker image in registry    (activemqint)  
7) vcell-activemqsim => docker image in registry    (activemqsim)  
8) vcell-clientgen   => docker image in registry    (generates Install4J installers during deployment)  
9) vcell-batch       => docker image in registry    (for batch processing, includes java code and Linux solver executables)  
10) vcell-batch.img  => singularity image in ./singularity-vm/   (built from vcell-batch docker image)  
11) vcell-opt =>     => docker image in registry    (opt)  

## (../swarm/README.md): build Singularity image for Linux solvers

builds a Singularity image named ./singularity-vm/${namespace}_vcell-batch_${tag}.img from the Docker image ${namespace}/vcell-batch:${tag}  

# 0. Choose VCell solvers version to include in build (Change only if new vcell-solvers build was committed)
**Tag vcell-solvers commit (if not already)**  

```
git clone https://github.com/virtualcell/vcell-solvers.git
cd vcell-solvers
//List current tags
git tag
//Create new tag by increment latest tag from list, must start with v, rest all digits, e.g. "git tag v0.0.22"
theNewTag=vx.x.x
git tag ${theNewTag)
//Push new tag to github, e.g. "git push origin v0.0.22"
git push origin ${theNewTag)
//github will alert travisci(mac,linux) and appveyor(win) to start building the tagged commit for client local solvers
//they will send their archived solvers to github and add to tagged commit (win64.zip,linux64.tgz,mac64.tgz)

```

----Wait for the win,linux and mac archives to appear on github (solvers built by travisci,appveyor, used on **VCell Local CLIENTS**) under the new commit tag (browse to https://github.com/virtualcell/vcell-solvers/releases/tag/${theNewTag})  
----Wait for ${theNewTag} to appear on dockerhub (builds solvers used on **VCell SERVER**), (browse to https://hub.docker.com/r/virtualcell/vcell-solvers/tags)

**Check Solver build finished (if necessary)** https://hub.docker.com/r/virtualcell/vcell-solvers/builds/ and check tag exists https://hub.docker.com/r/virtualcell/vcell-solvers/tags/  
See [vcell-solvers README.md](C:\users\frm\VCellTrunkGitWorkspaceSolvers\README_tagging.md)  
--**Edit** {vcellroot}/docker/build/Dockerfile-batch-dev [Dockerfile-batch-dev](../build/Dockerfile-batch-dev)  
----theTag=the tag that was created during a separate vcell-solver commit process (See https://github.com/virtualcell/vcell-solvers.git, README.md)  
----Get the tag  from [dockerhub](https://hub.docker.com/r/virtualcell/vcell-solvers/tags/), pick the tag you want, usually latest  
----Change line: "FROM virtualcell/vcell-solvers:{theTag}" to be proper tag number  

**Edit** {vcellroot}/vcell-core/pom.xml [vcell-core pom](../../vcell-core/pom.xml)  
----theTag= created as above  
----Get the tag from [github][https://github.com/virtualcell/vcell-solvers/tags), pick the tag you want, usually latest  
----Change all lines "https://github.com/virtualcell/vcell-solvers/releases/download/v{theTag}/{linux,win,mac}64.tgz", pick the tag you want, usually latest  

**MUST commit any changes made during above to github on the VCell project**  

#### 1.  Build VCell containers (from {vcell\_project\_dir}/docker/build/ directory)

**Login to vcell-node1.cam.uchc.edu as user vcell**  

**Get VCell project**, login to {theBuildHost} as user 'vcell'

```bash
theBuildHost=vcell-node1.cam.uchc.edu
ssh vcell@${theBuildHost}
cd /opt/build
rm -rf vcell (if necessary)
git clone https://github.com/virtualcell/vcell.git
cd vcell/docker/build
```

**Build ALL containers** (sets the Docker tags to first 7 characters of Git commit hash)

```bash
export VCELL_TAG=`git rev-parse HEAD | cut -c -7`
theRegistryHost=vcell-docker.cam.uchc.edu
export VCELL_REPO_NAMESPACE=${theRegistryHost}:5000/schaff
echo $VCELL_TAG $VCELL_REPO_NAMESPACE
./build.sh all $VCELL_REPO_NAMESPACE $VCELL_TAG
```

Info: build the containers (e.g. vcell-docker.cam.uchc.edu:5000/schaff/vcell-api:f18b7aa) and upload to a private Docker registry (e.g. vcell-docker.cam.uchc.edu:5000).  
A Singularity image for vcell-batch is also generated and stored locally (VCELL_ROOT/docker/build/singularity-vm) as no local Singularity repository is available yet.  Singularity image is downloaded by solver .slurm.sub script to the server file system and invoked for numerical simulation on the HPC cluster. 


# 2.  Deploy vcell using docker swarm mode


//Requirements during deployment
while building the vcell-clientgen container  
it is assumed that during deployment there is a directory (/usr/local/deploy/.install4j6/jres) which is mapped to the VOLUME /jre   
defined in Dockerfile-clientgen-dev and used inside vcell-clientgen container, the vcset up "build secrets" directory  
(e.g. /usr/local/deploy/.install4j6/jres/ on vcell-node1.cam.uchc.edu) Java jre bundles which are compatible with installed version of Install4J  
-----/usr/local/deploy/.install4j6/jres/linux-amd64-1.8.0_66.tar.gz  
-----/usr/local/deploy/.install4j6/jres/macosx-amd64-1.8.0_66.tar.gz  
-----/usr/local/deploy/.install4j6/jres/windows-x86-1.8.0_66.tar.gz  
-----/usr/local/deploy/.install4j6/jres/linux-x86-1.8.0_66.tar.gz  
-----/usr/local/deploy/.install4j6/jres/windows-amd64-1.8.0_66.tar.gz  



#### Build VCell and deploy to production servers (from ./docker/swarm/ directory)  
NOTE: current partition of SLURM for vcell is found by this command run "sinfo -N -h -p vcell2 --Format='nodelist'" (must run on vcell-service, or other slurm node)  
Assume step 1 has completed successfully  

**login to vcell-node1 as user 'vcell'**



```bash
cd /opt/build/vcell/docker/swarm
```

**Run** the following bash commands in your terminal (sets the Docker tags to first 7 characters of Git commit hash)

```bash
export VCELL_TAG=`git rev-parse HEAD | cut -c -7`
export VCELL_REPO_NAMESPACE=vcell-docker.cam.uchc.edu:5000/schaff
```

**Determine build number for deploying**  
-----**a.** Get currently deployed client

```bash
echo Alpha `curl --silent http://vcell.org/webstart/Alpha/updates.xml | xmllint --xpath '//updateDescriptor/entry/@newVersion' - | awk '{print $1;}'` && \
echo Beta `curl --silent http://vcell.org/webstart/Beta/updates.xml | xmllint --xpath '//updateDescriptor/entry/@newVersion' - | awk '{print $1;}'` && \
echo Rel `curl --silent http://vcell.org/webstart/Rel/updates.xml | xmllint --xpath '//updateDescriptor/entry/@newVersion' - | awk '{print $1;}'`
```

-----**b.** Create final build number  
if deploy server only, theBuildNumber=(number from above)  
----theBuildNumber=number from above (the 4th digit),  e.g. **Alpha newVersion="7.0.0.51" theBuildNumber=51**  
If deploying client, theBuildNumber=(number from above) + 1  
----theBuildNumber= 1 + number from above (the 4th digit),  e.g. **Alpha newVersion="7.0.0.51" theBuildNumber=52**  
edit 'VCELL_BUILD='theBuildNumber in the appropriate site block below  

**To create deploy configuration file, Choose the site block being deployed**  
Info: create deploy configuration file (e.g. Test 7.0.0 build 8) file for server. Note that some server configuration is hard-coded in the **serverconfig-uch.sh** script.  

**MUST EDIT VCELL_BUILD=${theBuildNumber} to be correct**  

**REL**  

```bash
export VCELL_VERSION=7.0.0 VCELL_BUILD=${theBuildNumber} VCELL_SITE=rel
export MANAGER_NODE=vcellapi.cam.uchc.edu
export VCELL_INSTALLER_REMOTE_DIR="/share/apps/vcell3/apache_webroot/htdocs/webstart/Rel"
export VCELL_CONFIG_FILE_NAME=server_${VCELL_SITE}_${VCELL_VERSION}_${VCELL_BUILD}_${VCELL_TAG}.config
./serverconfig-uch.sh $VCELL_SITE $VCELL_REPO_NAMESPACE \
  $VCELL_TAG $VCELL_VERSION $VCELL_BUILD $VCELL_CONFIG_FILE_NAME
```

**BETA (not used)**  

```bash
export VCELL_VERSION=7.0.0 VCELL_BUILD=10 VCELL_SITE=beta
export MANAGER_NODE=vcellapi-beta.cam.uchc.edu
export VCELL_INSTALLER_REMOTE_DIR="/share/apps/vcell3/apache_webroot/htdocs/webstart/Beta"
export VCELL_CONFIG_FILE_NAME=server_${VCELL_SITE}_${VCELL_VERSION}_${VCELL_BUILD}_${VCELL_TAG}.config
./serverconfig-uch.sh $VCELL_SITE $VCELL_REPO_NAMESPACE \
  $VCELL_TAG $VCELL_VERSION $VCELL_BUILD $VCELL_CONFIG_FILE_NAME
```

**ALPHA**  

```bash
export VCELL_VERSION=7.0.0 VCELL_BUILD=51 VCELL_SITE=alpha
export MANAGER_NODE=vcellapi-beta.cam.uchc.edu
export VCELL_INSTALLER_REMOTE_DIR="/share/apps/vcell3/apache_webroot/htdocs/webstart/Alpha"
export VCELL_CONFIG_FILE_NAME=server_${VCELL_SITE}_${VCELL_VERSION}_${VCELL_BUILD}_${VCELL_TAG}.config
./serverconfig-uch.sh $VCELL_SITE $VCELL_REPO_NAMESPACE \
  $VCELL_TAG $VCELL_VERSION $VCELL_BUILD $VCELL_CONFIG_FILE_NAME
```

**TEST**  

```bash
export VCELL_VERSION=7.0.0 VCELL_BUILD=7 VCELL_SITE=test
export MANAGER_NODE=vcellapi-beta.cam.uchc.edu
export VCELL_INSTALLER_REMOTE_DIR="/share/apps/vcell3/apache_webroot/htdocs/webstart/Test"
export VCELL_CONFIG_FILE_NAME=server_${VCELL_SITE}_${VCELL_VERSION}_${VCELL_BUILD}_${VCELL_TAG}.config
./serverconfig-uch.sh $VCELL_SITE $VCELL_REPO_NAMESPACE \
  $VCELL_TAG $VCELL_VERSION $VCELL_BUILD $VCELL_CONFIG_FILE_NAME
```

**Finalize Deploy**  
configuration and Docker images, generate client installers and deploy server as a Docker stack in swarm mode.  Note that the Docker and Singularity images and docker-compose.yml file remain independent of the deployed configuration.  Only the final deployed configuration file vcellapi.cam.uchc.edu:/usr/local/deploy/config/$VCELL_CONFIG_FILE_NAME contains server dependencies.  get platform installer from web site (e.g. http://vcell.org/webstart/Test/VCell_Test_macos_7.0.0_7_64bit.dmg)

Choose 1 of the following:

**CLIENT and SERVER** deploy commands (may request password at some point)

```bash
rm -rf ./generated_installers
./generate_installers.sh ./${VCELL_CONFIG_FILE_NAME}

// Login apple-id (Apple ID account page)
     Signup for 2 factor authentication (do this only once)->
     Under ‘Security’ section Create app specific password called “altoolpw”  (only visible if you are using 2 fact auth)->
//Store ‘altoolpw’ in local keychain (do this only once)
     xcrun altool --list-providers -u "frm@uchc.edu" -p "@keychain:altoolpw"
//Check ‘altoolpw’ 
     xcrun altool --list-providers -u "frm@uchc.edu" -p "@keychain:altoolpw"
//Copy MacOS installer built by install4j (to Mac with xcode and credentials installed)
     scp vcell@vcell-node1:/opt/build/vcell/docker/swarm/generated_installers/VCell_Alpha_macos_7_2_0_51_64bit.dmg /Users/vcellbuild/Downloads
//Notarize request for the VCell MacOS .dmg created by install4j
     xcrun altool --notarize-app --primary-bundle-id "org.vcell.i4jmacos" --username "frm@uchc.edu" --password "@keychain:altoolpw" --file /Users/vcellbuild/Downloads/VCell_Alpha_macos_7_2_0_51_64bit.dmg
//Save the requestUUID if the process doesn’t fail
     RequestUUID = fad728cf-47f0-493b-b666-f11aa61932c1 (failed)
			       e6397f48-38a9-4285-adea-9e2221fe74d0 (failed, but fewer errors)
				7ff1cb03-ed6a-4572-8208-76cd59039db3 (success)
//Check Notarization status (wait ~5 minutes)
     xcrun altool --notarization-history 0 -u "frm@uchc.edu" -p "@keychain:altoolpw"
//Get full Notarization log url and view in web browser (if notarization status failed, will contain web address of error log)
     xcrun altool --notarization-info fad728cf-47f0-493b-b666-f11aa61932c1 -u "frm@uchc.edu" -p "@keychain:altoolpw"
//Staple Ticket to Software (if notarization status has ‘success’)
     xcrun stapler staple /Users/vcellbuild/Downloads/VCell_Alpha_macos_7_2_0_51_64bit.dmg
//Copy Notarized software back to deploy server (vcell-node1) and continue deployment
     scp /Users/vcellbuild/Downloads/VCell_Alpha_macos_7_2_0_51_64bit.dmg vcell@vcell-node1:/opt/build/vcell/docker/swarm/generated_installers/VCell_Alpha_macos_7_2_0_51_64bit.dmg
```

```bash
./deploy.sh \
   --ssh-user vcell --ssh-key ~/.ssh/id_rsa --install-singularity --build-installers --installer-deploy-dir $VCELL_INSTALLER_REMOTE_DIR --link-installers \
   ${MANAGER_NODE} \
   ./${VCELL_CONFIG_FILE_NAME} /usr/local/deploy/config/${VCELL_CONFIG_FILE_NAME} \
   ./docker-compose.yml        /usr/local/deploy/config/docker-compose_${VCELL_TAG}.yml \
   vcell${VCELL_SITE}
```

**SERVER only** deploy commands (may request password at some point)

```bash
./deploy.sh \
   --ssh-user vcell --ssh-key ~/.ssh/id_rsa --install-singularity  \
   ${MANAGER_NODE} \
   ./${VCELL_CONFIG_FILE_NAME} /usr/local/deploy/config/${VCELL_CONFIG_FILE_NAME} \
   ./docker-compose.yml        /usr/local/deploy/config/docker-compose_${VCELL_TAG}.yml \
   vcell${VCELL_SITE}
```

[Info Local Service Debugging](README_localServicesDebug.md)


/usr/local/deploy/config
sudo $(cat ${VCELL_CONFIG_FILE_NAME} | xargs) docker stack deploy -c docker-compose_${VCELL_TAG}.yml vcellalpha

