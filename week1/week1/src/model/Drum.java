package model;

public class Drum extends PercussionInst {

    //세트 여부
    private final boolean isSet;

    public Drum(String name, int price, String manufacturer, String material, String stickMaterial, boolean isSet) {

        super(name, price, manufacturer, material, stickMaterial);
        this.isSet = isSet;
    }

    public boolean isSet() {

        return isSet;
    }

    public void playBeat(){

        System.out.println(this.getName() + "로 신나는 비트를 연주합니다.");
    }

    @Override
    public void getInfo() {

        super.getInfo();
        System.out.println("세트 구성: " + (this.isSet ? "풀 세트" : "단품"));
    }

    @Override
    public void makeSound() {

        System.out.println(this.getName() + "이 큰 소리를 냅니다.");
    }
}
