import { Image, StyleSheet, View, Text, Button, TextInput } from "react-native";

import { SafeAreaView } from "react-native-safe-area-context";
import React, { useCallback, useRef, useState } from "react";
import {
  Territory,
  useCurrentTerritory,
  useVisibleTerritories,
} from "@/hooks/useTerritories";
import { BottomSheetModal, BottomSheetView } from "@gorhom/bottom-sheet";
import {
  useApproveMember,
  useCreateClan,
  useJoinClan,
  useMyClan,
  useSendResources,
} from "@/hooks/useClan";
import { useCurrentUser } from "@/hooks/useUser";
import { COLORS } from "@/lib/colors";
import { SendResources } from "./SendResources";

export const ClanView = () => {
  const { data: user, isLoading: isUserLoading } = useCurrentUser();
  const { data: clan, isLoading: isClanLoading } = useMyClan();

  const [selectedUser, setSelectedUser] = useState<{ id: string; nick: string }>();

  const bottomSheetModalRef = useRef<BottomSheetModal>(null);
  const handlePresentModalPress = useCallback((user: { id: string; nick: string }) => {
    setSelectedUser(user);
    bottomSheetModalRef.current?.present();
  }, []);

  const { mutate: approveUser } = useApproveMember(clan?.id);

  if (isClanLoading || !clan) {
    return (
      <View>
        <Text>Loading...</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <View style={styles.clan}>
        <Text>{clan.name}</Text>
        <Text>Members:</Text>
        {clan.users.map((user) => (
          <View key={user.id}>
            <Text>{user.nick}</Text>
            <Button
              title="send resources"
              onPress={() => handlePresentModalPress(user)}
            />
          </View>
        ))}
        {clan.adminId === user?.id && (
          <View>
            <Text>User to approve</Text>
            {clan.users.map((user) => (
              <View key={user.id} style={styles.user}>
                <Text>{user.nick}</Text>
                <Button
                  title="accept"
                  onPress={() => {
                    approveUser({ userId: user.id });
                  }}
                />
              </View>
            ))}
          </View>
        )}
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
          <SendResources receiverNick={selectedUser?.nick!} />
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
  user: {},
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
