package org.cloudsdale.response.v2;

/**
 * Created by tyr on 25/09/2013.
 */
public abstract class CollectionResponse {

    private Collection collection;

    public class Collection {

        private Relative next;
        private Relative prev;

        public Relative getNext() {
            return next;
        }

        public Relative getPrev() {
            return prev;
        }
    }

    public static class Relative {

        private int time;
        private int limit;
        private int offest;

        public int getTime() {
            return time;
        }

        public int getLimit() {
            return limit;
        }

        public int getOffest() {
            return offest;
        }
    }

}
