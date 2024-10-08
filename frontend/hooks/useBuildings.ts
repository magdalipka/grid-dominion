import { request } from "@/lib/request";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useEffect } from "react";

export type Building = {
  id: string;
  level: number;
  goldCost: number;
  woodCost: number;
  foodCost: number;
  bonus: number;
  type: string;
};

export const useUpgradeBuilding = () => {
  const client = useQueryClient();

  return useMutation({
    mutationFn: async ({ territoryId, buildingId,userId }: { territoryId: number, buildingId: number, userId : string }) => {
      const res = await (
        await request("/buildings/upgrade", {
          method: "POST",
          headers: {
            "content-type": "application/json",
          },
          body: JSON.stringify({
            territoryId,
            buildingId,
            userId
          }),
        })
      ).json();
      console.log({ res });
      return res;
    },
    onMutate: () => {
      client.refetchQueries({ queryKey: ["territories"] });
      client.refetchQueries({ queryKey: ["myInventory"] });
    },
  });
};

