package Containers;

import java.util.List;

public class SideBarData {
    final private List<UserData> userData;
    final private List<String> chatData;

    public SideBarData(List<UserData> userData, List<String> chatData) {
        this.userData = userData;
        this.chatData = chatData;
    }
}
