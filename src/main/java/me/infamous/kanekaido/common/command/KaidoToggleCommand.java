package me.infamous.kanekaido.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;

public class KaidoToggleCommand {
   public static void register(CommandDispatcher<CommandSource> pDispatcher) {
      final LiteralArgumentBuilder<CommandSource> literalArgumentBuilder = Commands.literal("kaido").requires((source) -> {
         return source.hasPermission(2);
      });
      GameRules.visitGameRuleTypes(new GameRules.IRuleEntryVisitor() {
         public <T extends GameRules.RuleValue<T>> void visit(GameRules.RuleKey<T> pKey, GameRules.RuleType<T> pType) {
            literalArgumentBuilder.then(Commands.literal(pKey.getId()).executes((context) -> {
               return KaidoToggleCommand.queryRule(context.getSource(), pKey);
            }).then(pType.createArgument("value").executes((p_223482_1_) -> {
               return KaidoToggleCommand.setRule(p_223482_1_, pKey);
            })));
         }
      });
      pDispatcher.register(literalArgumentBuilder);
   }

   private static <T extends GameRules.RuleValue<T>> int setRule(CommandContext<CommandSource> context, GameRules.RuleKey<T> ruleKey) {
      CommandSource source = context.getSource();
      T t = source.getServer().getGameRules().getRule(ruleKey);
      t.setFromArgument(context, "value");
      source.sendSuccess(new TranslationTextComponent("commands.kanekaido.kaido.toggle", ruleKey.getId(), t.toString()), true);
      return t.getCommandResult();
   }

   private static <T extends GameRules.RuleValue<T>> int queryRule(CommandSource source, GameRules.RuleKey<T> ruleKey) {
      T t = source.getServer().getGameRules().getRule(ruleKey);
      source.sendSuccess(new TranslationTextComponent("commands.kanekaido.kaido.query", ruleKey.getId(), t.toString()), false);
      return t.getCommandResult();
   }
}