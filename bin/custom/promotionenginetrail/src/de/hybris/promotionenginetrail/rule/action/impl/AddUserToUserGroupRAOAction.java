package de.hybris.promotionenginetrail.rule.action.impl;

import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.ChangeUserGroupRAO;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import de.hybris.platform.ruleengineservices.rao.UserGroupRAO;
import de.hybris.platform.ruleengineservices.rao.UserRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import de.hybris.platform.ruleengineservices.rule.evaluation.actions.AbstractRuleExecutableSupport;
import de.hybris.platform.ruleengineservices.rule.evaluation.actions.RAOAction;
import de.hybris.platform.ruleengineservices.util.RAOConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AddUserToUserGroupRAOAction extends AbstractRuleExecutableSupport implements RAOAction
{
	private static final Logger LOG = LoggerFactory.getLogger(AddUserToUserGroupRAOAction.class);
	@Override
	public boolean performActionInternal(final RuleActionContext context)
	{
		final String userGroupCode = context.getParameter(RAOConstants.VALUE_PARAM, String.class);
		LOG.info("userGroupCode " + userGroupCode);
		performAction(context, userGroupCode);
		return true;
	}

	protected void performAction(final RuleActionContext context, final String userGroupCode)
	{
		final CartRAO cartRao = context.getCartRao();
		final RuleEngineResultRAO result = context.getRuleEngineResultRao();

		final ChangeUserGroupRAO changeUserGroupRAO = new ChangeUserGroupRAO();
		getRaoUtils().addAction(cartRao, changeUserGroupRAO);
		changeUserGroupRAO.setUserGroupId(userGroupCode);
		result.getActions().add(changeUserGroupRAO);

		final UserGroupRAO userGroupRAO = new UserGroupRAO();
		userGroupRAO.setId(userGroupCode);
		final UserRAO user = cartRao.getUser();
		user.getGroups().add(userGroupRAO);

		setRAOMetaData(context, changeUserGroupRAO);
		context.insertFacts(changeUserGroupRAO, userGroupRAO);
		context.scheduleForUpdate(user);
	}
}