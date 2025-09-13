import model.*;
import thread.Customer;
import thread.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    //모든 스레드가 공유해야하는 정보(악기 재고)
    private final List<Instrument> instrumentList = new ArrayList<>();

    private final List<Thread> activeThreads = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        Main shop = new Main();
        shop.runShop();
    }

    public void runShop() {

        setupInventory();
        startBackgroundThreads(); // 직원 스레드를 미리 실행
        boolean isOpen = true;

        while (isOpen) {
            displayMenu();
            System.out.print(">> ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewInventory();
                    break;
                case "2":
                    tryInstrument();
                    break;
                case "3":
                    spawnCustomer();
                    break;
                case "4":
                    isOpen = false;
                    shutdown();
                    break;
                default:
                    System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
            }
        }
        System.out.println("가게 문을 닫습니다. 프로그램을 종료합니다.");
    }

    // 초기 재고 설정
    private void setupInventory() {
        System.out.println("가게를 열고 악기 재고를 준비합니다...");
        instrumentList.add(new Guitar("펜더 텔레캐스터", 180, "Fender", 6, "메이플", "애쉬", "싱글코일"));
        instrumentList.add(new Guitar("깁슨 레스폴", 320, "Gibson", 6, "마호가니", "마호가니", "험버커"));
        instrumentList.add(new Bass("펜더 액티브 재즈베이스", 80, "Fender", 4, "메이플"));
        instrumentList.add(new Drum("롤랜드 V-Drum", 450, "Roland", "메쉬", "플라스틱", true));
    }

    // 직원 같은 백그라운드 스레드를 미리 실행
    private void startBackgroundThreads() {
        System.out.println("직원 스레드가 악기 관리를 시작합니다...");
        Employee employee = new Employee("직원 Henry", instrumentList);
        Thread employeeThread = new Thread(employee);
        employeeThread.start();
        activeThreads.add(employeeThread);
    }

    // 메뉴 출력
    private void displayMenu() {
        System.out.println("\n===== Henry 악기점 =====");
        System.out.println("1. 재고 확인");
        System.out.println("2. 악기 연주하기");
        System.out.println("3. 손님 생성하기");
        System.out.println("4. 프로그램 종료");
        System.out.println("===============");
    }

    // 재고 확인 기능
    private void viewInventory() {
        System.out.println("\n--- 현재 재고 목록 ---");
        for (Instrument instrument : instrumentList) {
            instrument.getInfo();
            System.out.println("판매 상태: " + (instrument.isSold() ? "판매 완료" : "판매 중"));
            System.out.println("--------------------");
        }
    }

    // 사용자가 악기 연주를 시도하는 기능
    private void tryInstrument() {
        System.out.println("몇 번 악기를 연주하시겠습니까? (번호 입력)");
        for (int i = 0; i < instrumentList.size(); i++) {
            System.out.printf("%d: %s\n", i + 1, instrumentList.get(i).getName());
        }
        System.out.print(">> ");

        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            Instrument instrument = instrumentList.get(index);

            if (instrument.isSold()) {
                System.out.println("이미 판매된 악기입니다.");
            } else if (instrument instanceof StringInst) {
                StringInst stringInst = (StringInst) instrument;
                if (stringInst.isBeingTuned()) {
                    System.out.println("!!! 직원 스레드가 현재 이 악기를 조율 중이라 연주할 수 없습니다. !!!");
                } else {
                    instrument.makeSound();
                }
            } else {
                instrument.makeSound();
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("잘못된 번호입니다.");
        }
    }

    // 사용자가 손님 스레드를 생성하는 기능
    private void spawnCustomer() {
        System.out.println("어떤 손님을 생성할까요? (예시: 손님 존 메이어, 500)");
        System.out.println("(이름, 예산 순으로 쉼표로 구분하여 입력)");
        System.out.print(">> ");

        try {
            String[] input = scanner.nextLine().split(",");
            String name = input[0].trim();
            int budget = Integer.parseInt(input[1].trim());

            // 판매되지 않은 악기 중 하나를 무작위로 선택
            List<Instrument> availableItems = new ArrayList<>();
            for(Instrument item : instrumentList) {
                if(!item.isSold()) {
                    availableItems.add(item);
                }
            }

            if (availableItems.isEmpty()) {
                System.out.println("판매할 수 있는 악기가 더 이상 없습니다.");
                return;
            }

            //랜덤 목표 악기 생성
            Random random = new Random();
            Instrument target = availableItems.get(random.nextInt(availableItems.size()));

            Customer customer = new Customer(name, budget, target);
            Thread customerThread = new Thread(customer);
            customerThread.start();
            activeThreads.add(customerThread);
            System.out.printf("--> %s 님이 %s에 관심을 보이며 입장합니다.\n", name, target.getName());

        } catch (Exception e) {
            System.out.println("입력 형식이 잘못되었습니다. 예시: 손님 존 메이어, 500");
        }
    }

    // 프로그램 종료 처리
    private void shutdown() {

        System.out.println("마감 준비 중... 모든 스레드를 종료합니다.");
        for (Thread thread : activeThreads) {
            thread.interrupt();
        }
        scanner.close();
    }
}