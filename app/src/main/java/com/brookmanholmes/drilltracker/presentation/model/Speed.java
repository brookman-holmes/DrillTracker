package com.brookmanholmes.drilltracker.presentation.model;

/**
 * Created by brookman on 1/25/18.
 */

public enum Speed {
    TWO_DIAMOND_SOFT {
        @Override
        public float getDiamondOffset() {
            return -2f;
        }
    },
    DIAMOND_SOFT {
        @Override
        public float getDiamondOffset() {
            return -1f;
        }
    },
    HALF_DIAMOND_SOFT {
        @Override
        public float getDiamondOffset() {
            return -.5f;
        }
    },
    QUARTER_DIAMOND_SOFT {
        @Override
        public float getDiamondOffset() {
            return -.25f;
        }
    },
    CORRECT {
        @Override
        public float getDiamondOffset() {
            return 0;
        }
    },
    QUARTER_DIAMOND_HARD {
        @Override
        public float getDiamondOffset() {
            return .25f;
        }
    },
    HALF_DIAMOND_HARD {
        @Override
        public float getDiamondOffset() {
            return .5f;
        }
    },
    DIAMOND_HARD {
        @Override
        public float getDiamondOffset() {
            return 1.0f;
        }
    },
    TWO_DIAMOND_HARD {
        @Override
        public float getDiamondOffset() {
            return 2f;
        }
    };

    public abstract float getDiamondOffset();
}
