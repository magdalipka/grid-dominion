import { Image, StyleSheet, View, Text, Button, TextInput } from "react-native";

import { SafeAreaView } from "react-native-safe-area-context";
import React, { useCallback, useRef, useState } from "react";
import {
  Territory,
  useCurrentTerritory,
  useVisibleTerritories,
} from "@/hooks/useTerritories";
import { BottomSheetModal, BottomSheetView } from "@gorhom/bottom-sheet";
import { useMyClan } from "@/hooks/useClan";
import { useCurrentUser, useLogout } from "@/hooks/useUser";
import { COLORS } from "@/lib/colors";
import { NoClanView } from "@/components/clan/NoClanView";
import { ClanView } from "@/components/clan/ClanView";

export default function SettingsScreen() {
  const { data: user, isLoading: isUserLoading } = useCurrentUser();
  const { mutate: logout } = useLogout();

  if (isUserLoading) {
    return (
      <View>
        <Text>Loading...</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <Button title="Log out" onPress={() => logout()} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    display: "flex",
    alignContent: "center",
    alignItems: "center",
    justifyContent: "space-around",
    height: "100%",
    width: "100%",
  },
});
