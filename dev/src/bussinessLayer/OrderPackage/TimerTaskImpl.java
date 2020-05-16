package bussinessLayer.OrderPackage;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import ServiceLayer.OrderService;
import ServiceLayer.ServiceObjects.ScheduledDTO;

public class TimerTaskImpl extends TimerTask {
    private final Timer timer;
    private Date nextDate;
    private ScheduledDTO scheduled;

    public TimerTaskImpl(Timer timer, Date nextDate, ScheduledDTO scheduled){
        this.timer = timer;
        this.nextDate = nextDate;
        this.scheduled = scheduled;
    }

    @Override
    public void run() {
        if(!scheduledOrderExist(scheduled.getSupplierId(), nextDate)){
            this.cancel();
            return;
        }
        if(orderExist()){
            return;
        }
        OrderService.getInstance().createScheduledOrder(scheduled, nextDate);
        this.nextDate = java.sql.Timestamp.valueOf(LocalDateTime.now().plusDays(7));
        //this.nextDate = getNextDateToCreateOrder(scheduled.getDay());
        timer.schedule(this, this.nextDate);

    }

    private boolean scheduledOrderExist(int supplierId, Date nextDate2) {
        return false;//TODO IS SCHEDULED EXIST IN DB
    }

    private boolean orderExist() {
        return false;//TODO IS ORDER EXIST IN TABLE IN THE SAME DAY
    }

    private Date getNextDateToCreateOrder(DayOfWeek day) {
        int difference = Math.abs(day.getValue()-Calendar.getInstance().get(Calendar.DAY_OF_WEEK))-1;
        return java.sql.Timestamp.valueOf(LocalDateTime.now().plusDays(difference));
    }
    
}