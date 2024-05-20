import { Tabs } from "expo-router";
import React from "react";

import { TabBarIcon } from "@/components/navigation/TabBarIcon";
import { Colors } from "@/constants/Colors";
import { useColorScheme } from "@/hooks/useColorScheme";
import { useAuth } from "@/contexts/auth";
import { TextInput, View, Text, Button, Pressable } from "react-native";

export default function TabLayout() {
  const colorScheme = useColorScheme();
  const { isLoading, user, login, register } = useAuth();

  const [nick, onNickInput] = React.useState("");
  const [password, onPasswordInput] = React.useState("");

  const [hasAccount, setHasAccount] = React.useState(true);

  if (isLoading) {
    return (
      <View>
        <Text>Loading...</Text>
      </View>
    );
  }

  if (!user) {
    return (
      <View>
        <View>
          <Text>Nick</Text>
          <TextInput onChangeText={onNickInput} value={nick} />
        </View>
        <View>
          <Text>Password</Text>
          <TextInput onChangeText={onPasswordInput} value={password} />
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
    );
  }

  return (
    <Tabs
      screenOptions={{
        tabBarActiveTintColor: Colors[colorScheme ?? "light"].tint,
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
    </Tabs>
  );
}
