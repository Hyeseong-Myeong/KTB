package model;

public class StringInst extends Instrument {

    private final int stringNum;

    private final String neckWood;

    private boolean isTuned = false;

    private boolean isBeingTuned = false;


    public StringInst(String name, int price, String manufacturer, int stringNum, String neckWood) {

        super(name, price, manufacturer);
        this.stringNum = stringNum;
        this.neckWood = neckWood;
    }

    public synchronized void tune(){

        if (isBeingTuned) {
            System.out.printf("[%s] 이미 다른 직원이 조율 중입니다.\n", getName());
            return;
        }

        this.isBeingTuned = true;
        System.out.printf("[%s] 조율을 시작합니다...\n", getName());

        try {
            // 조율에 2초가 소요
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        this.isTuned = true;
        this.isBeingTuned = false;
        System.out.printf("[%s]조율 완료! \n", getName());
    }

    public synchronized void detune(){

        System.out.printf("[%s] 연주로 인해 튜닝이 틀어졌습니다.\n", getName());
        this.isTuned = false;
    }

    public synchronized boolean isTuned() {

        return this.isTuned;
    }

    public synchronized boolean isBeingTuned() {
        return this.isBeingTuned;
    }

    @Override
    public void getInfo(){

        super.getInfo();
        System.out.println("줄 개수: " + this.stringNum);
        System.out.println("넥 재질: " + this.neckWood);
        System.out.println("조율 상태: " + (this.isTuned ? "조율 완료" : "조율 필요함"));
    }
}
