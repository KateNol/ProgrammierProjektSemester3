package logic;

import java.util.*;

public class DemoPlayer implements Player{
    @Override
    public void setName() {
        Scanner s = new Scanner(System.in);
        System.out.println("Gib deinen Namen ein:");
        name = s.next();

    }
}
}
