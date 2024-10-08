import { request } from "@/lib/request";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useCurrentUser } from "./useUser";

export const useMyClan = () => {
  const { data: user } = useCurrentUser();

  return useQuery({
    queryKey: ["myClan"],
    queryFn: async () => {
      const res = await (
        await request("/clans/" + user?.clanId, {
          method: "GET",
        })
      ).json();
      console.log({ res });
      return res as {
        id: string;
        name: string;
        isPrivate: boolean;
        users: Array<{ id: string; nick: string }>;
        usersToApprove: Array<{ id: string; nick: string }>;
        adminId: string;
      };
    },
    enabled: Boolean(user?.clanId),
  });
};

export const useCreateClan = () => {
  const client = useQueryClient();
  return useMutation({
    mutationFn: async ({ name }: { name: string }) => {
      const res = await (
        await request("/clans", {
          method: "POST",
          headers: {
            "content-type": "application/json",
          },
          body: JSON.stringify({
            name,
            isPrivate: false,
          }),
        })
      ).json();
      console.log({ res });
      return res;
    },
    onMutate: () =>
      setTimeout(() => {
        client.refetchQueries({ queryKey: ["currentUser"] });
        client.refetchQueries({ queryKey: ["myClan"] });
      }, 1000),
  });
};

export const useJoinClan = () => {
  console.log("here");
  return useMutation({
    mutationFn: async ({ clanId }: { clanId: string }) => {
      const res = await (
        await request("/clans/" + clanId + "/join", {
          method: "POST",
          headers: {
            "content-type": "application/json",
          },
        })
      ).text();
      console.log({ res });
      return res;
    },
  });
};

export const useApproveMember = (clanId?: string) => {
  const client = useQueryClient();
  return useMutation({
    mutationFn: async ({ userId }: { userId: string }) => {
      const res = await (
        await request("/clans/" + clanId + "/approveUser/" + userId, {
          method: "POST",
          headers: {
            "content-type": "application/json",
          },
        })
      ).json();
      console.log({ res });
      return res;
    },
    onMutate: () => {
      client.refetchQueries({ queryKey: ["currentUser"] });
      client.refetchQueries({ queryKey: ["myClan"] });
    },
  });
};

export const useSendResources = () => {
  const client = useQueryClient();
  return useMutation({
    mutationFn: async ({
      receiverNick,
      gold,
      wood,
      food,
    }: {
      receiverNick: string;
      gold: number;
      wood: number;
      food: number;
    }) => {
      const res = await (
        await request("/clans/sendResources", {
          method: "POST",
          headers: {
            "content-type": "application/json",
          },
          body: JSON.stringify({
            receiverNick,
            wood,
            gold,
            food,
          }),
        })
      ).json();
      console.log({ res });
      return res;
    },
    onMutate: () => {
      client.refetchQueries({ queryKey: ["myInventory"] });
    },
  });
};
