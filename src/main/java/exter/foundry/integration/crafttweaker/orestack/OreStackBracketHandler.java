package exter.foundry.integration.crafttweaker.orestack;

import java.util.List;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.BracketHandler;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IngredientAny;
import crafttweaker.zenscript.IBracketHandler;
import exter.foundry.api.recipe.matcher.OreMatcher;
import stanhebben.zenscript.compiler.IEnvironmentGlobal;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import stanhebben.zenscript.expression.ExpressionString;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.parser.Token;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.natives.IJavaMethod;
import stanhebben.zenscript.util.ZenPosition;

@BracketHandler(priority = 100)
public class OreStackBracketHandler implements IBracketHandler
{
    private class Symbol implements IZenSymbol
    {
        private final IEnvironmentGlobal environment;
        private final String name;

        public Symbol(IEnvironmentGlobal environment, String name)
        {
            this.environment = environment;
            this.name = name;
        }

        @Override
        public IPartialExpression instance(ZenPosition position)
        {
            return new ExpressionCallStatic(position, environment, method, new ExpressionString(position, name));
        }
    }

    public static IIngredient getOreStack(String name)
    {
        return new CrTOreStack(new OreMatcher(name, 1));
    }

    private final IZenSymbol symbolAny;

    private final IJavaMethod method;

    public OreStackBracketHandler()
    {
        symbolAny = CraftTweakerAPI.getJavaStaticFieldSymbol(IngredientAny.class, "INSTANCE");
        method = CraftTweakerAPI.getJavaMethod(OreStackBracketHandler.class, "getOreStack", String.class);
    }

    @Override
    public IZenSymbol resolve(IEnvironmentGlobal environment, List<Token> tokens)
    {
        if (tokens.size() == 1 && tokens.get(0).getValue().equals("*"))
        {
            return symbolAny;
        }

        if (tokens.size() > 2)
        {
            if (tokens.get(0).getValue().equals("orestack") && tokens.get(1).getValue().equals(":"))
            {
                StringBuilder substance_builder = new StringBuilder();
                int i;
                for (i = 2; i < tokens.size(); i++)
                {
                    Token token = tokens.get(i);
                    substance_builder.append(token.getValue());
                }

                String substance = substance_builder.toString();
                if (substance != null)
                {
                    return new Symbol(environment, substance_builder.toString());
                }
            }
        }

        return null;
    }
}
