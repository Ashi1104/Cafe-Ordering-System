package Cafe;

import java.util.ArrayList;

    public class Order {

        private ArrayList<MenuItem> items;
        private ArrayList<Integer> quantities;

        public Order() {

            items = new ArrayList<>();
            quantities = new ArrayList<>();
        }

        public void addItem(MenuItem item, int quantity) {

            for (int i = 0; i < items.size(); i++) {

                if(items.get(i).getName().equals(item.getName())) {
                    quantities.set(i, quantities.get(i) + quantity);
                    return;
                }
            }

            items.add(item);
            quantities.add(quantity);
        }

        public double calculateTotal() {

            double total = 0;

            for(int i = 0; i < items.size(); i++) {
                total += items.get(i).getPrice() * quantities.get(i);
            }

            return total;
        }

        public ArrayList<MenuItem> getItems() {
            return items;
        }

        public ArrayList<Integer> getQuantities() {
            return quantities;
        }

        public void clearOrder() {
            items.clear();
            quantities.clear();
        }

        public void removeItem(int index) {

            if (index >= 0 && index < items.size()) {
                items.remove(index);
                quantities.remove(index);
            }
        }

        public boolean isEmpty() {
            return items.isEmpty();
        }
    }
