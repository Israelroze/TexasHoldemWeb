package Card;

public enum CardSuit {
    Spades{
        @Override
        public String toString() {
            return "S";
        }
    },
    Hearts{
        @Override
        public String toString() {
            return "H";
        }
    },
    Diamonds {
        @Override
        public String toString() {
            return "D";
        }
    },
    Clubs{
        @Override
        public String toString() {
            return "C";
        }
    },
    UnknownSuit{
        @Override
        public String toString() {
            return "U";
        }
    }
}
