package ro.pub.cs.systems.eim.practicaltest02;

public class Information {
    int action_type;
    int hour;
    int minute;

    public Information() {
    }

    public Information(int action_type, int hour, int minute) {
        this.action_type = action_type;
        this.hour = hour;
        this.minute = minute;
    }
}
