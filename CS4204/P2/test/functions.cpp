

/*
 * Worker functions for testing para-pat impl.
 *
 */

int convertBoolToInt(bool b) {
    if (b) {
        return 1;
    } else {
        return 0;
    }
}

int alwaysReturnTwo(bool value) { return 2; }

bool swapBool(bool value) { return !value; }

int addOne(int value) { return value + 1; }