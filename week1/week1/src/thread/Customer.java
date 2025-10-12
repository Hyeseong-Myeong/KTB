package thread;

import model.Instrument;
import model.StringInst;

public class Customer implements Runnable {

    private final String name;
    private final int budget;
    private final Instrument wishlistItem;

    public Customer(String name, int budget, Instrument wishlistItem) {
        this.name = name;
        this.budget = budget;
        this.wishlistItem = wishlistItem;
    }

    @Override
    public void run() {

        try {
            System.out.printf("[%s] 가게에 입장했습니다.\n", this.name);
            Thread.sleep(1500);

            //연주 횟수를 기록
            //2회 연주 해 보기 전까지 구매할 수 없음
            int playCount = 0;

            // 연주 2번 성공할 때까지 루프 실행
            while (playCount < 2) {
                System.out.printf("[%s] %s을(를) %d번째 연주해보려 합니다.\n", this.name, wishlistItem.getName(), playCount + 1);
                Thread.sleep(2000);

                // 연주 시도 전, 악기 상태 확인
                if (wishlistItem.isSold()) {
                    System.out.printf("[%s] 연주해보려 했지만, 그새 팔렸네요...\n", this.name);
                    break;
                }

                if (wishlistItem instanceof StringInst) {
                    StringInst stringInst = (StringInst) wishlistItem;
                    if (stringInst.isBeingTuned()) {
                        System.out.printf("... [%s] 직원이 악기를 조율하고 있어서 잠시 기다립니다.\n", this.name);
                        Thread.sleep(3000);
                        continue;
                    }

                    if (!stringInst.isTuned()) {
                        System.out.printf("... [%s] 악기 튜닝이 안돼있어서 직원이 조율해줄 때까지 기다립니다.\n", this.name);
                        Thread.sleep(10000);
                        continue;
                    }
                }

                // 모든 조건을 통과하면 성공적으로 연주
                wishlistItem.makeSound();
                playCount++;

                // 연주 후 디튠
                if (wishlistItem instanceof StringInst) {
                    ((StringInst) wishlistItem).detune();
                }

                Thread.sleep(2000);
            }

            // 루프가 정상적으로 종료되었을 때만 구매 로직으로 진행
            if (playCount == 2) {
                System.out.printf("[%s] 충분히 연주해봤으니 구매를 결정해야겠어요.\n", this.name);
                Thread.sleep(2000);

                if (budget >= wishlistItem.getPrice()) {
                    boolean success = wishlistItem.sell();
                    if (success) {
                        System.out.printf("🎉[%s] %s 구매에 성공했습니다!\n", this.name, wishlistItem.getName());
                    } else {
                        System.out.printf("😭[%s] 구매하려고 했는데, 이미 팔렸네요.\n", this.name);
                    }
                } else {
                    System.out.printf("😭[%s] 예산이 부족해서 살 수 없네요...\n", this.name);
                }
            }

        } catch (InterruptedException e) {
            System.out.printf("[%s] 오늘은 그냥 돌아가야겠네요.\n", this.name);
            Thread.currentThread().interrupt();
        }

        System.out.printf("[%s] 가게를 나갑니다.\n", this.name);
    }
}