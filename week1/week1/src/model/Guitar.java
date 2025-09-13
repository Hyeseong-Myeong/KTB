package model;

public class Guitar extends StringInst {

    //바디 재질
    private final String bodyMaterial;

    //픽업 타입
    private final String pickupType;


    public Guitar(String name, int price, String manufacturer, int stringNum, String neckWood, String bodyMaterial, String pickupType) {

        super(name, price, manufacturer, stringNum, neckWood);
        this.bodyMaterial = bodyMaterial;
        this.pickupType = pickupType;
    }

    public String getBodyMaterial() {

        return bodyMaterial;
    }

    public String getPickupType() {

        return pickupType;
    }

    public void stroke(){

        System.out.println(this.getName() + "로 신나게 스트로크를 연주합니다.");
    }

    @Override
    public void getInfo() {

        super.getInfo();
        System.out.println("바디 재질: " + this.bodyMaterial);
        System.out.println("픽업 종류: " + this.pickupType);
    }

    @Override
    public void makeSound() {

        if (this.isTuned()) {
            System.out.println(this.getName() + "이(가) 맑은 소리를 냅니다. 띵~");
        } else {
            System.out.println("먼저 악기를 조율해야 합니다!");
        }
    }

}
