package realworld.api.config.security;

import com.msjc.analytics.common.data.PersonId;
import com.msjc.analytics.common.data.SecurityUser;
import com.msjc.analytics.common.data.TenantId;
import com.msjc.analytics.common.data.UserId;
import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.Value;

@Value
public class SecurityUserImpl implements SecurityUser {

  String username;
  String nickname;
  Option<TenantId> tenantId;
  Option<PersonId> personId;
  Option<UserId> userId;
  List<String> authorities;

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("SecurityUser{" + username);
    tenantId.forEach(v -> sb.append(", ").append(v));
    personId.forEach(v -> sb.append(", ").append(v));
    userId.forEach(v -> sb.append(", ").append(v));
    sb.append('}');
    return sb.toString();
  }
}
