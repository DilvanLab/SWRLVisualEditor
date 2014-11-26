package br.usp.icmc.dilvan.swrlEditor.server.swrleditor.manager;

import java.util.List;

import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.Errors;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.RuleEvents;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.RuleSet;
import br.usp.icmc.dilvan.swrlEditor.client.rpc.swrleditor.rule.Rule;

public interface SWRLManager {

    public boolean deleteRule(String ruleName);

    public List<String> getBuiltins();

    public List<String> getBuiltins(String selfCompletion, int maxTerms);

    public Errors getErrorsList(Rule rule, boolean isNew);

    public RuleEvents getRuleEvents(long version);

    public RuleSet getRules();

    public Long getVersion();

    public boolean hasBuiltin(String builtin);

    public boolean insertRule(String ruleName, Rule rule);

    public List<String> runRules();

    public boolean updateRule(String ruleName, String oldRuleName, Rule rule);
}
