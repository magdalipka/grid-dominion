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
import { useCurrentUser } from "@/hooks/useUser";
import { COLORS } from "@/lib/colors";
import { NoClanView } from "@/components/clan/NoClanView";
import { ClanView } from "@/components/clan/ClanView";

export default function ClanScreen() {
  const { data: user, isLoading: isUserLoading } = useCurrentUser();

  const { data: clan, isLoading: isClanLoading } = useMyClan();

  if (isUserLoading || isClanLoading) {
    return (
      <View>
        <Text>Loading...</Text>
      </View>
    );
  }

  if (!user!.clanId) {
    return <NoClanView />;
  }

  return <ClanView />;
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
  clan: {
    gap: 8,
  },
  form: {
    width: "100%",
    justifyContent: "center",
    gap: 8,
  },
  input: {
    padding: 8,
    backgroundColor: COLORS.backgroundDark,
  },
});
