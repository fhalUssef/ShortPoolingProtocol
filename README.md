# ShortPoolingProtocol

## This simple RestApi simulate Short Pooling Protocol 



```markdown
1. user can submit job through the endpoint /submitJob . 

2. then the server reply with the unique id assigned for that job and insert that id in Map that contain job not yet finished . 

3. the server after each 3 second it increase all job progress by 10% then if a job progress reached 100% it gonna be removed
   from not yet finished and will be added  to Map of the completed Jobs .  

4. user can use the id return by the server to check the progress of the given Job through the endPoint /checkJobStatus

5. user can see the completed jobs through the endpoint /jobsCompleted & not yet finished through /jobInProgress

```
