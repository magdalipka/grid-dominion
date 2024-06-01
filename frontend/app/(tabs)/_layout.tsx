import { Tabs } from "expo-router";
import React from "react";

import { TabBarIcon } from "@/components/navigation/TabBarIcon";
import { Colors } from "@/constants/Colors";
import { useColorScheme } from "@/hooks/useColorScheme";
import { useAuth } from "@/contexts/auth";
import {
  TextInput,
  Button,
  Pressable,
  Image,
  StyleSheet,
  View,
  Text,
} from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import { COLORS } from "@/lib/colors";
import * as Location from "expo-location";
import { request } from "@/lib/request";

export default function TabLayout() {
  const colorScheme = useColorScheme();
  const { isLoading, user, login, register } = useAuth();

  const [nick, onNickInput] = React.useState("");
  const [password, onPasswordInput] = React.useState("");

  const [hasAccount, setHasAccount] = React.useState(true);

  React.useEffect(() => {
    (async () => {
      await Location.enableNetworkProviderAsync();
      await Location.requestForegroundPermissionsAsync();
      await Location.requestBackgroundPermissionsAsync();
    })();
  }, []);

  // workaround until background fetch starts working
  React.useEffect(() => {
    const interval = setInterval(async () => {
      let location = await Location.getCurrentPositionAsync({});
      const res = await (
        await request("/users/coord", {
          method: "POST",
          headers: {
            "content-type": "application/json",
          },
          body: JSON.stringify({
            x: location.coords.longitude,
            y: location.coords.latitude,
          }),
        })
      ).json();
      console.log({ res, location });
    }, 1000 * 60);
    return () => clearInterval(interval);
  }, []);

  if (isLoading) {
    return (
      <View>
        <Text>Loading...</Text>
      </View>
    );
  }

  if (!user) {
    return (
      <SafeAreaView style={styles.container}>
        <View style={styles.auth}>
          <View>
            <Text>Nick</Text>
            <TextInput onChangeText={onNickInput} value={nick} style={styles.input} />
          </View>
          <View>
            <Text>Password</Text>
            <TextInput
              onChangeText={onPasswordInput}
              value={password}
              style={styles.input}
            />
          </View>
          {hasAccount ? (
            <Button
              title="login"
              onPress={() => {
                console.log({ nick, password });
                login({ nick, password });
              }}
            />
          ) : (
            <Button
              title="register"
              onPress={() => {
                console.log({ nick, password });
                register({ nick, password });
              }}
            />
          )}
          <Pressable onPress={() => setHasAccount(!hasAccount)}>
            {hasAccount ? (
              <Text>Don't have an account? Click here to register</Text>
            ) : (
              <Text>Already have an account? Click here to login</Text>
            )}
          </Pressable>
        </View>
      </SafeAreaView>
    );
  }

  return (
    <Tabs
      screenOptions={{
        headerShown: false,
      }}
    >
      <Tabs.Screen
        name="index"
        options={{
          title: "Home",
          tabBarIcon: ({ color, focused }) => (
            <TabBarIcon name={focused ? "home" : "home-outline"} color={color} />
          ),
        }}
      />
      <Tabs.Screen
        name="inventory"
        options={{
          title: "Inventory",
          tabBarIcon: ({ color, focused }) => (
            <TabBarIcon name={focused ? "cube" : "cube-outline"} color={color} />
          ),
        }}
      />
    </Tabs>
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
  auth: {
    flex: 1,
    gap: 4,
    display: "flex",
    justifyContent: "center",
  },
  input: {
    backgroundColor: COLORS.backgroundDark,
  },
});
