import { useSendResources } from "@/hooks/useClan";
import { useState } from "react";
import { View, TextInput, Button } from "react-native";

export const SendResources = ({ receiverNick }: { receiverNick: string }) => {
  const { mutate: sendResources } = useSendResources();

  const [wood, onWood] = useState("");
  const [gold, onGold] = useState("");
  const [food, onFood] = useState("");

  return (
    <View>
      <TextInput
        placeholder="wood"
        keyboardType="number-pad"
        value={wood}
        onChangeText={onWood}
      />
      <TextInput
        placeholder="gold"
        keyboardType="number-pad"
        value={gold}
        onChangeText={onGold}
      />
      <TextInput
        placeholder="food"
        keyboardType="number-pad"
        value={food}
        onChangeText={onFood}
      />
      <Button
        title="send"
        onPress={() => {
          sendResources({
            receiverNick,
            wood: Number(wood) || 0,
            gold: Number(gold) || 0,
            food: Number(food) || 0,
          });
        }}
      />
    </View>
  );
};
