package com.baeldung.lss.security;

import java.util.HashSet;
import java.util.Set;

public final class LockedUsers {
    private static final Set<String> lockedUsersSets = new HashSet<>();

    private LockedUsers() {
    }

    public static boolean isLocked(final String username) {
        return lockedUsersSets.contains(username);
    }

    public static void lock(final String username) {
        lockedUsersSets.add(username);
    }

}
