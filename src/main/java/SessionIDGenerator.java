
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class SessionIDGenerator {


    public static final int MAX_RANGE=10;

    private Map<String, List<String>> sessionIds= new ConcurrentHashMap<String, List<String>>();
    private Set<String> sessionIdsUsed = new HashSet<String>();
    private Set<String> sessionIdsUnused = new HashSet<String>();
    private Set<String> clients = new HashSet<String>();

    public Map<String, List<String>> getSessionIds() {
        return sessionIds;
    }


    public void setSessionIdsForIp(List<String> sessionIds, String clientId) {
        getSessionIds().put(clientId, sessionIds);
    }

    public List<String> giveRange(String clientId)
    {
        List<String> sessionIds = new ArrayList<String>();
        int count=0;
        while(count!=MAX_RANGE)
        {
            HashSet<String> temp = new HashSet<String>();
            if(sessionIdsUnused.size()>0)
            {
                for(String str: sessionIdsUnused)
                {
                    sessionIds.add(str);
                    sessionIdsUsed.add(str);
                    temp.add(str);
                    count++;
                }
            }
            sessionIdsUnused.removeAll(temp);
            if(count!=MAX_RANGE)
            {
                String sessionId=UUID.randomUUID().toString();
                sessionIds.add(sessionId);
                sessionIdsUsed.add(sessionId);
                count++;
            }
        }

        if(getSessionIds().containsKey(clientId))
        {
            List<String> stringList = getSessionIds().get(clientId);
            stringList.addAll(sessionIds);
            getSessionIds().put(clientId, stringList);
        }
        else
        {
            setSessionIdsForIp(sessionIds, clientId);
        }

        return sessionIds;
    }

    public void releaseSessions(String clientId)
    {
        List<String> sessions=getSessionIds().get(clientId);
        if(sessions!=null)
        {
            sessionIdsUnused.addAll(sessions);
        }
        getSessionIds().remove(clientId);
    }

    public void releaseSessions(String clientId, List<String> sessionIds)
    {
        List<String> sessions=getSessionIds().get(clientId);
        for(String sessionId:sessionIds) {
            if (sessions !=null) {
                if(sessions.contains(sessionId)) {
                    sessionIdsUnused.add(sessionId);
                    sessions.remove(sessionId);
                }
            }
        }
        getSessionIds().remove(clientId);
    }

    public String generateClientId() {

        while (true) {
            String clientId = UUID.randomUUID().toString();
        /*if (!clients.contains(clientId)) {
            clients.add(clientId);
            return clientId;
        }
        else
        {*/
            return clientId;
        //}
      }

    }

}
