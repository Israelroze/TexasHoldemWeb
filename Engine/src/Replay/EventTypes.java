package Replay;

public enum EventTypes {
    Flop{
        @Override
        public String toString() {
            return "Flop";
        }
    },River{
        @Override
        public String toString() {
            return "River";
        }
    },Turn{
        @Override
        public String toString() {
            return "Turn";
        }
    },Winner{
        @Override
        public String toString() {
            return "Winner";
        }
    },Move{
        @Override
        public String toString() {
            return "Move";
        }
    }
}
