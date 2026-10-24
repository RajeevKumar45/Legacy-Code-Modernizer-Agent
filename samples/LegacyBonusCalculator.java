/*
 * LegacyBonusCalculator.java
 *
 * ORIGINAL BASELINE - intentionally written in an old, legacy style.
 * Do not modernize this file. Later versions will be compared against it.
 *
 * Legacy traits kept on purpose:
 *   - deeply nested if/else blocks
 *   - "magic numbers" (0.05, 1000, 20 ...) written inline, no named constants
 *   - money stored as double instead of BigDecimal
 *   - invalid input silently returns 0 instead of reporting an error
 *   - hand-written checks in main() instead of a test framework
 *
 * INPUT ASSUMPTIONS
 *   years      whole years of service, expected 0 or more
 *   salary     annual base salary in currency units, expected above 0
 *   fullTime   true = full-time employee, false = part-time employee
 *
 * BONUS RULES
 *   Invalid input (years < 0 or salary <= 0)  -> 0
 *
 *   Full-time:
 *     years < 1                -> 0
 *     1 <= years < 5           -> 5% of salary
 *     5 <= years < 10          -> 10% of salary
 *     years >= 10              -> 15% of salary
 *       and if years >= 20     -> plus a flat 1000 loyalty bonus
 *
 *   Part-time:
 *     years < 2                -> 0
 *     2 <= years < 5           -> 2% of salary
 *     years >= 5               -> 5% of salary
 *
 * EXPECTED RESULTS (these are the cases run by main)
 *   years  salary   fullTime  bonus
 *   -----  -------  --------  ------
 *     0    50000.0  true         0.0   (less than 1 year)
 *     3    50000.0  true      2500.0   (5%)
 *     5    50000.0  true      5000.0   (10%, boundary)
 *     7    50000.0  true      5000.0   (10%)
 *    10    50000.0  true      7500.0   (15%, boundary)
 *    12    50000.0  true      7500.0   (15%)
 *    20    50000.0  true      8500.0   (15% + 1000, boundary)
 *    25    50000.0  true      8500.0   (15% + 1000)
 *     1    30000.0  false        0.0   (less than 2 years)
 *     3    30000.0  false      600.0   (2%)
 *     8    30000.0  false     1500.0   (5%)
 *    -1    50000.0  true         0.0   (invalid years)
 *     5        0.0  true         0.0   (invalid salary)
 *
 * HOW TO RUN (needs a JDK, from the samples folder)
 *   javac LegacyBonusCalculator.java
 *   java LegacyBonusCalculator
 */
public class LegacyBonusCalculator {

    // Calculates the yearly bonus for one employee.
    public static double calculateBonus(int years, double salary, boolean fullTime) {
        double bonus = 0;

        if (years >= 0) {
            if (salary > 0) {
                if (fullTime) {
                    if (years >= 1) {
                        if (years >= 5) {
                            if (years >= 10) {
                                bonus = salary * 0.15;
                                if (years >= 20) {
                                    bonus = bonus + 1000;
                                }
                            } else {
                                bonus = salary * 0.10;
                            }
                        } else {
                            bonus = salary * 0.05;
                        }
                    } else {
                        bonus = 0;
                    }
                } else {
                    if (years >= 2) {
                        if (years >= 5) {
                            bonus = salary * 0.05;
                        } else {
                            bonus = salary * 0.02;
                        }
                    } else {
                        bonus = 0;
                    }
                }
            } else {
                bonus = 0;
            }
        } else {
            bonus = 0;
        }

        return bonus;
    }

    // Runs every documented case and prints PASS or FAIL for each one.
    public static void main(String[] args) {
        int failures = 0;

        failures = failures + check(0, 50000.0, true, 0.0);
        failures = failures + check(3, 50000.0, true, 2500.0);
        failures = failures + check(5, 50000.0, true, 5000.0);
        failures = failures + check(7, 50000.0, true, 5000.0);
        failures = failures + check(10, 50000.0, true, 7500.0);
        failures = failures + check(12, 50000.0, true, 7500.0);
        failures = failures + check(20, 50000.0, true, 8500.0);
        failures = failures + check(25, 50000.0, true, 8500.0);
        failures = failures + check(1, 30000.0, false, 0.0);
        failures = failures + check(3, 30000.0, false, 600.0);
        failures = failures + check(8, 30000.0, false, 1500.0);
        failures = failures + check(-1, 50000.0, true, 0.0);
        failures = failures + check(5, 0.0, true, 0.0);

        if (failures == 0) {
            System.out.println("All cases passed.");
        } else {
            System.out.println(failures + " case(s) failed.");
        }
    }

    // Prints one result line. Returns 1 if the result is wrong, 0 if it is right.
    public static int check(int years, double salary, boolean fullTime, double expected) {
        double actual = calculateBonus(years, salary, fullTime);
        String type;
        if (fullTime) {
            type = "full-time";
        } else {
            type = "part-time";
        }

        if (actual == expected) {
            System.out.println("PASS  years=" + years + " salary=" + salary + " " + type
                    + " -> bonus=" + actual);
            return 0;
        } else {
            System.out.println("FAIL  years=" + years + " salary=" + salary + " " + type
                    + " -> bonus=" + actual + " (expected " + expected + ")");
            return 1;
        }
    }
}
