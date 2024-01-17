import java.util.*;
public class ShoppingCart {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Catalogue
        int productAPrice = 20;
        int productBPrice = 40;
        int productCPrice = 50;

        // Discounts
        int flat10DiscountThreshold = 200;
        int flat10DiscountAmount = 10;
        int bulk5DiscountQuantityThreshold = 10;
        double bulk5DiscountRate = 0.05;
        int bulk10DiscountQuantityThreshold = 20;
        double bulk10DiscountRate = 0.10;
        int tiered50DiscountTotalThreshold = 30;
        int tiered50DiscountSingleProductThreshold = 15;
        double tiered50DiscountRate = 0.50;

        // Fees
        int giftWrapFee = 1;
        int shippingFeePerPackage = 5;
        int unitsPerPackage = 10;

        // User input
        int quantityA = getInput("Enter the quantity of Product A: ", scanner);
        int quantityB = getInput("Enter the quantity of Product B: ", scanner);
        int quantityC = getInput("Enter the quantity of Product C: ", scanner);

        boolean isGiftWrapA = getYesNoInput("Is Product A wrapped as a gift? (yes/no): ", scanner);
        boolean isGiftWrapB = getYesNoInput("Is Product B wrapped as a gift? (yes/no): ", scanner);
        boolean isGiftWrapC = getYesNoInput("Is Product C wrapped as a gift? (yes/no): ", scanner);

        // Calculate totals
        int subtotal = quantityA * productAPrice + quantityB * productBPrice + quantityC * productCPrice;
        int totalQuantity = quantityA + quantityB + quantityC;

        // Apply discounts
        int discountAmount = calculateDiscount(subtotal, totalQuantity, flat10DiscountThreshold, flat10DiscountAmount,
                bulk5DiscountQuantityThreshold, bulk5DiscountRate, bulk10DiscountQuantityThreshold,
                bulk10DiscountRate, tiered50DiscountTotalThreshold, tiered50DiscountSingleProductThreshold,
                tiered50DiscountRate, productCPrice);

        // Apply fees
        int giftWrapTotalFee = calculateGiftWrapFee(isGiftWrapA, quantityA) +
                calculateGiftWrapFee(isGiftWrapB, quantityB) +
                calculateGiftWrapFee(isGiftWrapC, quantityC);

        int shippingFee = calculateShippingFee(totalQuantity, unitsPerPackage, shippingFeePerPackage);

        // Calculate final total
        int finalTotal = subtotal - discountAmount + shippingFee + giftWrapTotalFee;

        // Display receipt
        System.out.println("\nReceipt:");
        displayProductDetails("Product A", quantityA, productAPrice);
        displayProductDetails("Product B", quantityB, productBPrice);
        displayProductDetails("Product C", quantityC, productCPrice);

        System.out.println("\nSubtotal: $" + subtotal);
        System.out.println("Discount Applied: $" + discountAmount);
        System.out.println("Shipping Fee: $" + shippingFee);
        System.out.println("Gift Wrap Fee: $" + giftWrapTotalFee);
        System.out.println("\nTotal: $" + finalTotal);

        scanner.close();
    }

    private static int getInput(String message, Scanner scanner) {
        System.out.print(message);
        return scanner.nextInt();
    }

    private static boolean getYesNoInput(String message, Scanner scanner) {
        System.out.print(message);
        String input = scanner.next().toLowerCase();
        return input.equals("yes");
    }

    private static void displayProductDetails(String productName, int quantity, int price) {
        System.out.println(productName + " - Quantity: " + quantity + ", Total: $" + quantity * price);
    }

    private static int calculateDiscount(int subtotal, int totalQuantity, int flat10Threshold, int flat10Amount,
                                         int bulk5Threshold, double bulk5Rate, int bulk10Threshold, double bulk10Rate,
                                         int tiered50TotalThreshold, int tiered50SingleProductThreshold,
                                         double tiered50Rate, int productCPrice) {
        int discount = 0;

        // Flat 10% discount
        if (subtotal > flat10Threshold) {
            discount = Math.min(flat10Amount, subtotal);
        }

        // Bulk 5% discount
        if (totalQuantity > bulk5Threshold) {
            int bulk5Discount = (int) (subtotal * bulk5Rate);
            discount = Math.max(discount, bulk5Discount);
        }

        // Bulk 10% discount
        if (totalQuantity > bulk10Threshold) {
            int bulk10Discount = (int) (subtotal * bulk10Rate);
            discount = Math.max(discount, bulk10Discount);
        }

        // Tiered 50% discount
        if (totalQuantity > tiered50TotalThreshold) {
            int eligibleQuantity = Math.max(0, totalQuantity - tiered50SingleProductThreshold);
            int tiered50Discount = (int) (eligibleQuantity * tiered50Rate * productCPrice);
            discount = Math.max(discount, tiered50Discount);
        }

        return discount;
    }

    private static int calculateGiftWrapFee(boolean isGiftWrap, int quantity) {
        return isGiftWrap ? quantity : 0;
    }

    private static int calculateShippingFee(int totalQuantity, int unitsPerPackage, int shippingFeePerPackage) {
        int packages = (totalQuantity + unitsPerPackage - 1) / unitsPerPackage;
        return packages * shippingFeePerPackage;
    }
}
