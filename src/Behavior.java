public interface Behavior {
     public Group getGroup ();
     public void setGroup (Group group);

     public int getState ();
         public static final int IDLE = 0;
         public static final int RUNNING_INSIDE = 1;
         public static final int RUNNING_OUTSIDE = 2;

     public BehaviorEvent getStartEvent ();
     public void setStartEvent (BehaviorEvent mask);

     public BehaviorEvent getStopEvent ();
     public void setStopEvent (BehaviorEvent mask);

     public void start (BehaviorEvent event);
     public void running (BehaviorEvent event);
     public void stop (BehaviorEvent event);
     public void cancel (BehaviorEvent event);

}
