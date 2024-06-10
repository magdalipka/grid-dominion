import { Image, StyleSheet, View, Text, Button, TextInput } from "react-native";

import { SafeAreaView } from "react-native-safe-area-context";
import React, { useCallback, useRef, useState } from "react";
import {
  Territory,
  useCurrentTerritory,
  useVisibleTerritories,
} from "@/hooks/useTerritories";
import { BottomSheetModal, BottomSheetView } from "@gorhom/bottom-sheet";
import { useCreateClan, useJoinClan, useMyClan } from "@/hooks/useClan";
import { useCurrentUser } from "@/hooks/useUser";
import { COLORS } from "@/lib/colors";

export const NoClanView = () => {
  const [modalType, setModalType] = useState<"create" | "join">();

  const bottomSheetModalRef = useRef<BottomSheetModal>(null);
  const handlePresentModalPress = useCallback((type: "create" | "join") => {
    setModalType(type);
    bottomSheetModalRef.current?.present();
  }, []);

  const [clanId, onClanId] = useState("");
  const [clanName, onClanName] = useState("");

  const { mutate: createClan } = useCreateClan();
  const { mutate: joinClan } = useJoinClan();

  return (
    <View style={styles.container}>
      <View style={styles.clan}>
        <Text>You're not a part of any clan.</Text>
        <Button title="Join a clan" onPress={() => handlePresentModalPress("join")} />
        <Button
          title="Create own clan"
          onPress={() => handlePresentModalPress("create")}
        />
      </View>
      <BottomSheetModal
        ref={bottomSheetModalRef}
        snapPoints={["80%"]}
        // style={styles.container}
        enableDynamicSizing
        enablePanDownToClose
        enableDismissOnClose
      >
        <BottomSheetView style={bottomSheetStyles.contentContainer}>
          {modalType === "create" ? (
            <View style={styles.form}>
              <TextInput
                placeholder="clan name"
                style={styles.input}
                onChangeText={onClanName}
                value={clanName}
              />
              <Button
                title="create clan"
                onPress={() => createClan({ name: clanName })}
              />
            </View>
          ) : (
            <View style={styles.form}>
              <TextInput
                placeholder="clan id"
                style={styles.input}
                onChangeText={onClanId}
                value={clanId}
              />
              <Button
                title="request joining to clan"
                onPress={() => joinClan({ clanId })}
              />
            </View>
          )}
        </BottomSheetView>
      </BottomSheetModal>
    </View>
  );
};

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

const bottomSheetStyles = StyleSheet.create({
  contentContainer: {
    flex: 1,
    display: "flex",
    alignContent: "center",
    alignItems: "center",
    justifyContent: "space-around",
    height: "100%",
    width: "100%",
  },
});
