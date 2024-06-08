import { Image, StyleSheet, View, Text } from "react-native";

import { useAuth } from "@/contexts/auth";
import { SafeAreaView } from "react-native-safe-area-context";
import React from "react";

import { useInventory } from "@/hooks/useInventory";
import { useMyClan } from "@/hooks/useClan";
import { useCurrentUser } from "@/hooks/useUser";

export default function InventoryScreen() {
  const { data: user, isLoading: isUserLoading } = useCurrentUser();

  const { data: clan, isLoading: isClanLoading } = useMyClan();

  return (
    <SafeAreaView style={styles.container}>
      <Text>{JSON.stringify({ user, clan })}</Text>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  map: {
    width: "100%",
    height: "100%",
  },
});
