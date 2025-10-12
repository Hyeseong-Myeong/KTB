package model;

public class Instrument {

    private String name;

    private int price;

    private final String manufacturer;

    private boolean isSold = false;


    public Instrument(String name, int price, String manufacturer) {

        this.name = name;
        this.price = price;
        this.manufacturer = manufacturer;
    }

    //view information
    public void getInfo(){

        System.out.println("--- 악기 정보 ---");
        System.out.println("이름: " + this.name);
        System.out.println("가격: " + this.price + "원");
        System.out.println("제조사: " + this.manufacturer);
    }

    //change isSold status
    public synchronized boolean sell() {

        if (!isSold) {
            this.isSold = true;
            System.out.println(">> " + getName() + " 판매 완료! <<");
            return true; // 판매 성공
        }
        return false; // 이미 팔렸거나 판매 실패
    }

    public void makeSound(){

        System.out.println(this.name + "이(가) 소리를 냅니다.");
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public boolean isSold() {
        return isSold;
    }
}
