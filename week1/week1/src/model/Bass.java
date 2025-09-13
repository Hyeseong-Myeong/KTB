package model;

public class Bass extends StringInst {

    public Bass(String name, int price, String manufacturer, int stringNum, String neckWood) {

        super(name, price, manufacturer, stringNum, neckWood);
    }

    public void slap() {

        System.out.println(this.getName() + "로 슬랩을 연주합니다.");
    }

    @Override
    public void makeSound() {

        if (this.isTuned()) {
            System.out.println(this.getName() + "가 묵직한 저음을 냅니다.");
        } else {
            System.out.println("먼저 악기를 조율해야 합니다!");
        }
    }
}
