package Player;

public enum PlayerState {
    DEALER{
        @Override
        public String toString() {
            return "D";
        }
    },BIG{
        @Override
        public String toString() {
            return "B";
        }
    },SMALL{
        @Override
        public String toString() {
            return "S";
        }
    },NONE{
        @Override
        public String toString() {
            return " ";
        }
    }
}
