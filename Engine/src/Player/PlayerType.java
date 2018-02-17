package Player;

public enum PlayerType {
    HUMAN{
        @Override
        public String toString() {
            return "H";
        }
    },
    COMPUTER{
        @Override
        public String toString() {
            return "C";
        }
    }
}
