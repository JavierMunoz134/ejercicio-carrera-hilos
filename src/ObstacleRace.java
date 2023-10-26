import java.util.Scanner;
import java.util.Random;

public class ObstacleRace {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Número de corredores: ");
        int numRunners = scanner.nextInt();
        scanner.nextLine();  // Consumir la nueva línea

        Runner[] runners = new Runner[numRunners];

        for (int i = 0; i < numRunners; i++) {
            System.out.println("Corredor " + (i + 1));
            System.out.print("Símbolo: ");
            char symbol = scanner.nextLine().charAt(0);
            int baseSpeed = getAttributeValue("Velocidad base (1-5): ", 1, 5);
            int turbo = getAttributeValue("Turbo (1-5): ", 1, 5);
            int obstacleAvoidance = getAttributeValue("Probabilidad de evitar obstáculos (1-5): ", 1, 5);

            runners[i] = new Runner(symbol, baseSpeed, turbo, obstacleAvoidance);
        }

        Race race = new Race(runners);
        race.start();

        scanner.close();
    }

    private static int getAttributeValue(String message, int min, int max) {
        Scanner scanner = new Scanner(System.in);
        int value;
        do {
            System.out.print(message);
            value = scanner.nextInt();
        } while (value < min || value > max);
        return value;
    }
}

class Runner {
    private char symbol;
    private int baseSpeed;
    private int turbo;
    private int obstacleAvoidance;
    private int position;

    public Runner(char symbol, int baseSpeed, int turbo, int obstacleAvoidance) {
        this.symbol = symbol;
        this.baseSpeed = baseSpeed;
        this.turbo = turbo;
        this.obstacleAvoidance = obstacleAvoidance;
    }

    public char getSymbol() {
        return symbol;
    }

    public int getPosition() {
        return position;
    }

    public void move() {
        Random random = new Random();
        int moveDistance = baseSpeed + (random.nextInt(100) < turbo * 10 ? baseSpeed : 0);
        int obstacleChance = random.nextInt(100);
        if (obstacleChance >= (100 - obstacleAvoidance * 10)) {
            moveDistance = 0;
        }
        position += moveDistance;
    }
}

class Race {
    private Runner[] runners;
    private int raceLength = 100;  // Longitud de la carrera
    private boolean raceOver = false;

    public Race(Runner[] runners) {
        this.runners = runners;
    }

    public void start() {
        while (!raceOver) {
            for (Runner runner : runners) {
                runner.move();
                if (runner.getPosition() >= raceLength) {
                    raceOver = true;
                }
            }

            printRaceState();
            try {
                Thread.sleep(1000);  // Esperar un segundo antes del siguiente turno
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Runner winner = findWinner();
        System.out.println("El vencedor es el corredor " + winner.getSymbol());
    }

    private void printRaceState() {
        for (int i = 0; i <= raceLength; i++) {
            for (Runner runner : runners) {
                if (runner.getPosition() == i) {
                    System.out.print(runner.getSymbol());
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    private Runner findWinner() {
        Runner winner = runners[0];
        for (Runner runner : runners) {
            if (runner.getPosition() > winner.getPosition()) {
                winner = runner;
            }
        }
        return winner;
    }
}
