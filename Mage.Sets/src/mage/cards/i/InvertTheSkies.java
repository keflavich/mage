/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.cards.i;

import java.util.Set;
import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.condition.LockedInCondition;
import mage.abilities.condition.common.ManaWasSpentCondition;
import mage.abilities.decorator.ConditionalContinuousEffect;
import mage.abilities.effects.ContinuousEffectImpl;
import mage.abilities.effects.common.InfoEffect;
import mage.abilities.effects.common.continuous.GainAbilityControlledEffect;
import mage.abilities.keyword.FlyingAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.ColoredManaSymbol;
import mage.constants.Duration;
import mage.constants.Layer;
import mage.constants.Outcome;
import mage.constants.SubLayer;
import mage.filter.common.FilterCreaturePermanent;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.watchers.common.ManaSpentToCastWatcher;

/**
 *
 * @author jeffwadsworth

 */
public class InvertTheSkies extends CardImpl {

    public InvertTheSkies(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.INSTANT},"{3}{G/U}");


        // Creatures your opponents control lose flying until end of turn if {G} was spent to cast Invert the Skies, and creatures you control gain flying until end of turn if {U} was spent to cast it.
        this.getSpellAbility().addEffect(new ConditionalContinuousEffect(
                new InvertTheSkiesEffect(),
                new LockedInCondition(new ManaWasSpentCondition(ColoredManaSymbol.G)),
                "Creatures your opponents control lose flying until end of turn if {G} was spent to cast {this},"));
        this.getSpellAbility().addEffect(new ConditionalContinuousEffect(
                new GainAbilityControlledEffect(FlyingAbility.getInstance(), Duration.EndOfTurn),
                new LockedInCondition(new ManaWasSpentCondition(ColoredManaSymbol.U)),
                "and creatures you control gain flying until end of turn if {U} was spent to cast it"));
        this.getSpellAbility().addEffect(new InfoEffect("<i>(Do both if {G}{U} was spent.)</i>"));
        this.getSpellAbility().addWatcher(new ManaSpentToCastWatcher());
    }

    public InvertTheSkies(final InvertTheSkies card) {
        super(card);
    }

    @Override
    public InvertTheSkies copy() {
        return new InvertTheSkies(this);
    }
}

class InvertTheSkiesEffect extends ContinuousEffectImpl {

    private static FilterCreaturePermanent filter = new FilterCreaturePermanent();

    public InvertTheSkiesEffect() {
        super(Duration.EndOfTurn, Layer.AbilityAddingRemovingEffects_6, SubLayer.NA, Outcome.LoseAbility);
    }

    public InvertTheSkiesEffect(final InvertTheSkiesEffect effect) {
        super(effect);
    }

    @Override
    public InvertTheSkiesEffect copy() {
        return new InvertTheSkiesEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Set<UUID> opponents = game.getOpponents(source.getControllerId());
        for (Permanent perm: game.getBattlefield().getActivePermanents(filter, source.getControllerId(), game)) {
            if (opponents.contains(perm.getControllerId())) {
                perm.getAbilities().remove(FlyingAbility.getInstance());
            }
        }
        return true;
    }

}