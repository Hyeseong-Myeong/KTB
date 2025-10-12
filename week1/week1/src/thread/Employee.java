package thread;

import model.Instrument;
import model.StringInst;

import java.util.List;

public class Employee implements Runnable {

    private final String name;
    private final List<Instrument> instrumentList;

    public Employee(String name, List<Instrument> instrumentList) {

        this.name = name;
        this.instrumentList = instrumentList;
    }

    @Override
    public void run() {

        try {
            while (!Thread.currentThread().isInterrupted()) {

                System.out.printf("[%s] 악기 상태를 점검합니다...\n", this.name);

                for (Instrument instrument : instrumentList) {

                    //현악기 && 판매되지 않음 && 조율이 필요 => 조율 실행
                    if (instrument instanceof StringInst stringInst) {
                        if (!stringInst.isSold() && !stringInst.isTuned()) {
                            stringInst.tune();
                        }
                    }
                }

                //30초마다 점검
                Thread.sleep(30000);
            }
        } catch (InterruptedException e) {

            System.out.printf("[%s] 퇴근합니다.\n", this.name);
            Thread.currentThread().interrupt();
        }
    }
}