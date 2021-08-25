package com.brookmanholmes.drilltracker.presentation.model;

/**
 * Created by brookman on 1/25/18.
 */

public enum SpeedResult {
    TWO_DIAMOND_SOFT {
        @Override
        public float getDiamondOffset() {
            return -2f;
        }

        @Override
        public boolean isSoftHit() {
            return true;
        }
    },
    DIAMOND_SOFT {
        @Override
        public float getDiamondOffset() {
            return -1f;
        }

        @Override
        public boolean isSoftHit() {
            return true;
        }
    },
    HALF_DIAMOND_SOFT {
        @Override
        public float getDiamondOffset() {
            return -.5f;
        }

        @Override
        public boolean isSoftHit() {
            return true;
        }
    },
    QUARTER_DIAMOND_SOFT {
        @Override
        public float getDiamondOffset() {
            return -.25f;
        }

        @Override
        public boolean isSoftHit() {
            return true;
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

        @Override
        public boolean isHardHit() {
            return true;
        }
    },
    HALF_DIAMOND_HARD {
        @Override
        public float getDiamondOffset() {
            return .5f;
        }

        @Override
        public boolean isHardHit() {
            return true;
        }
    },
    DIAMOND_HARD {
        @Override
        public float getDiamondOffset() {
            return 1.0f;
        }

        @Override
        public boolean isHardHit() {
            return true;
        }
    },
    TWO_DIAMOND_HARD {
        @Override
        public float getDiamondOffset() {
            return 2f;
        }

        @Override
        public boolean isHardHit() {
            return true;
        }
    };

    public abstract float getDiamondOffset();

    public boolean isSoftHit() {
        return false;
    }

    public boolean isHardHit() {
        return false;
    }
}
