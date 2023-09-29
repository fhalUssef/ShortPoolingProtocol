package protocols.demo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    private Map<String , Double > jobInProcess = new HashMap<>() ; 
    private Map<String , Integer > completedJob = new HashMap<>() ; 
    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @PostConstruct
    public void start(){
        System.out.println("Jobs Processing Started");
        startProcessingJobs();
    }

    @GetMapping("/submitJob")
    public long  submitJob(){
        long timeId = System.currentTimeMillis() ; 
        jobInProcess.put(  String.valueOf(timeId) , 0d) ; 
        return   timeId  ; 
    }

    public void startProcessingJobs() {
        executorService.execute(() -> {
            while (true) {
                jobInProcess.replaceAll((key, value) -> value + 0.10 );
                updateJobsStatus();
                try {
                    Thread.sleep(1000); // Sleep for 10 seconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }


    public void updateJobsStatus(){
        Iterator<Map.Entry<String, Double>> iterator = jobInProcess.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Double> entry = iterator.next();
            String s = entry.getKey();
            double progress = entry.getValue();
                     
            if (progress >= 1) {
                iterator.remove(); // Remove the entry using the iterator
                completedJob.put(s, 1); 
            }
        }
    }

    @GetMapping("/getJobStatus")
    public String getJobStatus ( @RequestParam String id ){
        String status  = "progress : "; 
        if(completedJob.containsKey(id))
            status += completedJob.get(id)*100 + "%" ;
        else if ( jobInProcess.containsKey(id) )
            status +=  jobInProcess.get(id)*100 + "%" ;
        else
            status = " job not found" ;
        return status ; 
    }

    @GetMapping("/jobInProgress")
    public String getJobStatus(){
        StringBuilder notFinishedJobs = new StringBuilder( "Still in Process ").append("\n") ; 
        for( String c : jobInProcess.keySet() )
            notFinishedJobs.append(c + " progress " + jobInProcess.get(c)*100).append("%").append("\n") ;  
        return notFinishedJobs.toString() ; 
    }

    @GetMapping("/jobsCompleted")
    public String getCompletedJob(){
        StringBuilder finishedJobs = new StringBuilder("Completed Jobs : ").append("\n") ; 
        for( String c : completedJob.keySet() )
            finishedJobs.append(c + " progress " + completedJob.get(c)*100).append("% ").append("\n") ; 
        return finishedJobs.toString() ; 
    }
    
}
