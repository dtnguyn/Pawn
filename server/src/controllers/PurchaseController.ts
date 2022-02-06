import { Purchase } from "../entity/Purchase";
import { getRepository } from "typeorm";
import { User } from "../entity/User";

const isPurchaseValid = async (token: string) => {
  const repo = getRepository(Purchase);

  const results = await repo.find({ purchaseToken: token });

  return results.length == 0;
};

export const executePurchase = async (
  token: string,
  time: string,
  orderId: string,
  userId: string
) => {
  const result = await isPurchaseValid(token);

  if (result) {
    //Make a purchase

    const repo = getRepository(Purchase);

    await repo.insert({
      orderId,
      purchaseTime: time,
      isValid: true,
      purchaseToken: token,
    });

    const userRepo = getRepository(User);
    await userRepo.update({ id: userId }, { isPremium: true });

    return true;
  } else {
    //Return false
    return false;
  }
};
