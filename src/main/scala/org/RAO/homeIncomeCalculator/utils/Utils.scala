package org.RAO.homeIncomeCalculator.utils

object Utils
{
  def required(input: Map[String, Any], params: List[String])=
  {
    for{ param <-params if (!input.contains(param))} yield param
  }
}
